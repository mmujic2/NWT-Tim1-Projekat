package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import the.convenient.foodie.restaurant.config.DiscountFeignClient;
import the.convenient.foodie.restaurant.dto.FilterRestaurantRequest;
import the.convenient.foodie.restaurant.dto.RestaurantWithRating;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.ReviewRepository;
import the.convenient.foodie.restaurant.repository.custom.RestaurantRepositoryCustom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DiscountFeignClient discountFeignClient;

    @Override
    public Page<RestaurantWithRating> getRestaurants(FilterRestaurantRequest filters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);
        Metamodel m = entityManager.getMetamodel();
        EntityType<Restaurant> Restaurant_ = m.entity(Restaurant.class);

        Root<Restaurant> restaurant = cq.from(Restaurant.class);
        List<Predicate> listOfPredicates = new ArrayList<>();

        CriteriaQuery<Long> parallelCq = cb.createQuery(Long.class);
        Root<Restaurant> parallelRoot = parallelCq.from(Restaurant.class);
        List<Predicate> parallelListOfPredicates = new ArrayList<>();

        List<Restaurant> results = new ArrayList<>();
        List<RestaurantWithRating> restaurantsWithRating = new ArrayList<>();

        System.out.println("FILTERI");
        System.out.println(filters);
        if(filters!=null) {
            if (filters.getName()!=null && !filters.getName().isBlank()) {
                listOfPredicates.add(cb.like(restaurant.get("name"), "%" + filters.getName() + "%"));
                parallelListOfPredicates.add(cb.like(parallelRoot.get("name"), "%" + filters.getName() + "%"));
            }

            if (filters.getCategoryIds()!=null && !filters.getCategoryIds().isEmpty()) {
                System.out.println(filters.getCategoryIds());
                Join<Restaurant, Category> categories = restaurant.join("categories");
                Join<Restaurant, Category> parallelCategories = parallelRoot.join("categories");
                filters.getCategoryIds().stream().forEach(
                        categoryId -> {
                            listOfPredicates.add( cb.equal(cb.literal(categoryId),categories.get("id")));
                            parallelListOfPredicates.add(cb.equal(cb.literal(categoryId), parallelCategories.get("id")));
                        }
                );
                System.out.println(listOfPredicates);
            }


            if (filters.getIsOfferingDiscount()!=null && Boolean.valueOf(filters.getIsOfferingDiscount())) {
                cq.select(restaurant).where(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
                results.addAll(getResults(cq, pageable));
                //Handle filtering parallel query for count
                //Call Discount MS

                var headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                var jsonObject = "{restaurants: [" + results.stream().map(r-> "\""+r.getUuid()+"\"").collect(Collectors.joining(",")) +"]}";
                System.out.println(jsonObject);
                HttpEntity<String> request =
                        new HttpEntity<String>(jsonObject.toString(), headers);
                var res = discountFeignClient.filterDiscountedRestaurants(results.stream().map(r-> r.getUuid()).collect(Collectors.toList()));
                System.out.println(res);
            }
            System.out.println(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
        }



        if(pageable.getSort()!=null) {
            pageable.getSort().forEach(order ->
            {
                if (order.getDirection().isAscending()) {
                    switch (order.getProperty()) {
                        case "NAME":
                            cq.orderBy(cb.asc(restaurant.get("name")));
                            break;

                        case "DATE":
                            cq.orderBy(cb.asc(restaurant.get("created")));
                            break;


                    }

                    if(results.isEmpty()) {
                        cq.select(restaurant).where(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
                        results.addAll(getResults(cq, pageable));
                    }

                    if (order.getProperty().equals("POPULARITY")) {
                        var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                        //Call Order Microservice
                    } else if (order.getProperty().equals("RATING")) {
                        results.stream().forEach(r -> {
                            var rating = reviewRepository.calculateAverageRatingForRestaurant(r.getId());

                            var restaurantWithRating = new RestaurantWithRating(r,rating!=null?rating:0.0);
                            restaurantsWithRating.add(restaurantWithRating);
                        });
                        restaurantsWithRating.sort(Comparator.comparing(RestaurantWithRating::getRating));

                    }


                } else {
                    switch (order.getProperty()) {
                        case "NAME":
                            cq.orderBy(cb.desc(restaurant.get("name")));
                            break;

                        case "DATE":
                            cq.orderBy(cb.desc(restaurant.get("created")));
                            break;


                    }

                    if(results.isEmpty()) {
                        cq.select(restaurant).where(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
                        results.addAll(getResults(cq,pageable));
                    }

                    if (order.getProperty().equals("POPULARITY")) {
                        var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                        //Call Order Microservice
                    } else if (order.getProperty().equals("RATING")) {
                        results.stream().forEach(r -> {

                            var rating = reviewRepository.calculateAverageRatingForRestaurant(r.getId());
                            var restaurantWithRating = new RestaurantWithRating(r,rating!=null?rating:0.0);
                            restaurantsWithRating.add(restaurantWithRating);
                        });
                        restaurantsWithRating.sort(Comparator.comparing(RestaurantWithRating::getRating).reversed());

                    }
                }

            });

        }

        if(results.isEmpty()) {
            cq.select(restaurant).where(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
            results.addAll(getResults(cq, pageable));
            System.out.println(results);
            System.out.println(listOfPredicates.toArray(new Predicate[listOfPredicates.size()]));
        }



        parallelCq.select(cb.countDistinct(parallelRoot.get("id"))).where(parallelListOfPredicates.toArray(new Predicate[parallelListOfPredicates.size()]));
        var numberOfRecords = entityManager.createQuery(parallelCq).getSingleResult();
        System.out.println(numberOfRecords);

        if(restaurantsWithRating.isEmpty()) {
            results.stream().forEach(r -> {
                var rating = reviewRepository.calculateAverageRatingForRestaurant(r.getId());
                var restaurantWithRating = new RestaurantWithRating(r, rating!=null?rating:0.0);
                restaurantsWithRating.add(restaurantWithRating);
            });
        }

        return new PageImpl<>(restaurantsWithRating, pageable, numberOfRecords);
    }

    private List<Restaurant> getResults(CriteriaQuery cq, Pageable pageable) {
        Integer offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();
        return entityManager.createQuery(cq).setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
    }


}

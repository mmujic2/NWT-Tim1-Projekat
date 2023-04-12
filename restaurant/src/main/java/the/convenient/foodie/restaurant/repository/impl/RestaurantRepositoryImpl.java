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
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.ReviewRepository;
import the.convenient.foodie.restaurant.repository.custom.RestaurantRepositoryCustom;
import the.convenient.foodie.restaurant.repository.metamodel.Restaurant_;

import java.util.*;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Page<RestaurantWithRating> getRestaurants(FilterRestaurantRequest filters, Pageable pageable) {
        var hql = "SELECT new the.convenient.foodie.restaurant.dto.RestaurantWithRating(r,avg(rev.rating))"
                + " FROM Restaurant r LEFT JOIN Review rev ON r.id=rev.restaurant.id";

        var results = new ArrayList<RestaurantWithRating>();

        Map<String,String> whereClauseMap = new HashMap<>();


        if(filters!=null) {
            if (filters.getName()!=null && !filters.getName().isBlank()) {
                whereClauseMap.put("name"," LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) ");
            }

            if (filters.getCategoryIds()!=null && !filters.getCategoryIds().isEmpty()) {
                whereClauseMap.put("category"," EXISTS(SELECT c FROM Category c " +
                        "WHERE c.id IN (:categoryIds) AND c MEMBER OF r.categories)");
            }

        }

        if(!whereClauseMap.isEmpty()) {
            hql += " WHERE " + whereClauseMap.values().stream().collect(Collectors.joining(" AND "));
        }



        if (filters!=null && filters.getIsOfferingDiscount()!=null && Boolean.valueOf(filters.getIsOfferingDiscount())) {
            var query = entityManager.createQuery(hql,RestaurantWithRating.class);

            if(whereClauseMap.containsKey("name"))
                query.setParameter("name",filters.getName());

            if(whereClauseMap.containsKey("category"))
                query.setParameter("categoryIds",filters.getCategoryIds());

            Integer offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();


            results = (ArrayList<RestaurantWithRating>) query.setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
            //Handle filtering parallel query for count
            //Call Discount MS
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var jsonObject = "{restaurants: [" + results.stream().map(r-> "\""+r.getUuid()+"\"").collect(Collectors.joining(",")) +"]}";
            System.out.println(jsonObject);
            HttpEntity<String> request =
                    new HttpEntity<String>(jsonObject.toString(), headers);

            //var res = restTemplate.postForObject("http://discount-service/coupon/filter",request, String.class);
            List<String> uuids = results.stream().map(r-> r.getUuid()).collect(Collectors.toList());
            //var res = discountFeignClient.filterDiscountedRestaurants(uuids);
            var res = restTemplate.getForObject("http://discount-service/coupon/all",String.class);
            System.out.println(res);
        }

        hql += " GROUP BY r";



        if(pageable.getSort()!=null && !pageable.getSort().isEmpty()) {
            var order = pageable.getSort().stream().collect(Collectors.toList()).get(0);
            if(order.getDirection().isAscending()) {
                if (order.getDirection().isAscending()) {
                    switch (order.getProperty()) {
                        case "NAME":
                            hql += " ORDER BY r.name ASC";
                            break;

                        case "DATE":
                            hql += " ORDER BY r.created ASC";
                            break;
                        case "RATING":
                            hql += " ORDER BY avg(rev.rating) ASC";
                            break;

                    }

                    if(results.isEmpty()) {
                        results.addAll(getResults(hql,pageable,whereClauseMap.containsKey("name")? filters.getName():null,whereClauseMap.containsKey("category")?filters.getCategoryIds():null));
                    }

                    if (order.getProperty().equals("POPULARITY")) {
                        var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                        //Call Order Microservice
                    }


                } else {
                    switch (order.getProperty()) {
                        case "NAME":
                            hql += " ORDER BY r.name DESC";
                            break;

                        case "DATE":
                            hql += " ORDER BY r.created DESC";
                            break;
                        case "RATING":
                            hql += " ORDER BY avg(rev.rating) DESC";
                            break;


                    }

                    if(results.isEmpty()) {
                        results.addAll(getResults(hql,pageable,whereClauseMap.containsKey("name")? filters.getName():null,whereClauseMap.containsKey("category")?filters.getCategoryIds():null));
                    }

                    if (order.getProperty().equals("POPULARITY")) {
                        var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                        //Call Order Microservice
                    }
                }

            }
        }

        if(results.isEmpty()) {
            results.addAll(getResults(hql,pageable,whereClauseMap.containsKey("name")? filters.getName():null,whereClauseMap.containsKey("category")?filters.getCategoryIds():null));
        }



        return new PageImpl<>(results, pageable, 10L);

    }


    public Page<RestaurantWithRating> getRestaurants2(FilterRestaurantRequest filters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);


        Root<Restaurant> restaurant = cq.from(Restaurant.class);
        List<Predicate> listOfPredicates = new ArrayList<>();

        CriteriaQuery<Long> parallelCq = cb.createQuery(Long.class);
        Root<Restaurant> parallelRoot = parallelCq.from(Restaurant.class);
        List<Predicate> parallelListOfPredicates = new ArrayList<>();

        List<Restaurant> results = new ArrayList<>();
        List<RestaurantWithRating> restaurantsWithRating = new ArrayList<>();
        if(filters!=null) {
            if (filters.getName()!=null && !filters.getName().isBlank()) {
                listOfPredicates.add(cb.like(restaurant.get("name"), "%" + filters.getName() + "%"));
                parallelListOfPredicates.add(cb.like(parallelRoot.get("name"), "%" + filters.getName() + "%"));
            }

            if (filters.getCategoryIds()!=null && !filters.getCategoryIds().isEmpty()) {
                /*var restaurants = categoryRepository.getRestaurantsWithCategories(filters.getCategoryIds());
                cq.select(restaurant);
                results.clear();
                var queryResult = getResults(cq,pageable);
                results.addAll(queryResult.stream().filter(r -> restaurants.stream().map(r1->r1.getId()).collect(Collectors.toList()).contains(r.getId())).collect(Collectors.toList()));*/
                Join<Restaurant, Category> categories = restaurant.join("categories");
                Join<Restaurant, Category> parallelCategories = parallelRoot.join("categories");
                Predicate[] subqueryPredicates = new Predicate[2];
                List<Predicate> listOfSubqueryPredicates = new ArrayList<>();
                filters.getCategoryIds().stream().forEach(
                        categoryId -> {
                            Subquery<Category> subquery = cq.subquery(Category.class);
                            Root<Category> category = subquery.from(Category.class);
                            subqueryPredicates[0] = cb.equal(cb.literal(categoryId), category.get("id"));
                            subqueryPredicates[1] = category.in(categories);
                            subquery.select(category).where(subqueryPredicates);
                            if(!listOfSubqueryPredicates.isEmpty())
                                listOfSubqueryPredicates.set(0,cb.or(listOfSubqueryPredicates.get(0),cb.exists(subquery)));
                            parallelListOfPredicates.add(cb.equal(cb.literal(categoryId), parallelCategories.get("id")));
                        }
                );
                if(!listOfSubqueryPredicates.isEmpty())
                    listOfPredicates.add(listOfSubqueryPredicates.get(0));

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

    private List<RestaurantWithRating> getResults(String hql, Pageable pageable,String name, List<Long> categoryIds) {
        System.out.println(hql);
        System.out.println(name);
        System.out.println(categoryIds);
        var query = entityManager.createQuery(hql,RestaurantWithRating.class);

        if(name!=null)
            query.setParameter("name",name);

        if(categoryIds!=null)
            query.setParameter("categoryIds",categoryIds);

        Integer offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        return query.setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
    }




}

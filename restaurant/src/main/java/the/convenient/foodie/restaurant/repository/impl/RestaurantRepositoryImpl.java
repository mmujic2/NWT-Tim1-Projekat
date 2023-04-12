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
import the.convenient.foodie.restaurant.config.OrderFeignClient;
import the.convenient.foodie.restaurant.dto.FilterRestaurantRequest;
import the.convenient.foodie.restaurant.dto.RestaurantWithRating;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.ReviewRepository;
import the.convenient.foodie.restaurant.repository.custom.RestaurantRepositoryCustom;

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
    private OrderFeignClient orderFeignClient;

    @Autowired
    private CategoryRepository categoryRepository;


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

        hql += " GROUP BY r";

        if (filters!=null && filters.getIsOfferingDiscount()!=null && Boolean.valueOf(filters.getIsOfferingDiscount())) {

            results = (ArrayList<RestaurantWithRating>) getResults(hql,pageable,whereClauseMap.containsKey("name")?filters.getName():null,whereClauseMap.containsKey("category")?filters.getCategoryIds():null);

            //Call Discount MS

            List<String> uuids = results.stream().map(r-> r.getUuid()).collect(Collectors.toList());
            List<String> res = discountFeignClient.filterDiscountedRestaurants(uuids);

            results = (ArrayList<RestaurantWithRating>) results.stream().filter(r -> res.contains(r.getUuid())).collect(Collectors.toList());
        }



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
                        var res = orderFeignClient.getNumberOfOrdersPerRestaurant(resultUUIDs,"asc");
                        System.out.println(res);
                    }


                } else {
                    System.out.println("DESCEND");
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
                    System.out.println(order.getProperty());
                    if (order.getProperty().equals("POPULARITY")) {
                        System.out.println("UŠLO");
                        var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                        //Call Order Microservice
                        var res = orderFeignClient.getNumberOfOrdersPerRestaurant(resultUUIDs,"desc");
                        System.out.println(res);
                    }
                }

            }
        }

        if(results.isEmpty()) {
            results.addAll(getResults(hql,pageable,whereClauseMap.containsKey("name")? filters.getName():null,whereClauseMap.containsKey("category")?filters.getCategoryIds():null));
        }



        return new PageImpl<>(results, pageable, 10L);

    }

    @Override
    public String getRestaurantUUID(Long id) {
        var hql = "SELECT r.uuid FROM Restaurant r WHERE r.id=:id";
        return entityManager.createQuery(hql, String.class).setParameter("id",id).getSingleResult();
    }

    private List<RestaurantWithRating> getResults(String hql, Pageable pageable,String name, List<Long> categoryIds) {
        System.out.println(hql);
        var query = entityManager.createQuery(hql,RestaurantWithRating.class);

        if(name!=null)
            query.setParameter("name",name);

        if(categoryIds!=null)
            query.setParameter("categoryIds",categoryIds);

        Integer offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        return query.setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
    }





}

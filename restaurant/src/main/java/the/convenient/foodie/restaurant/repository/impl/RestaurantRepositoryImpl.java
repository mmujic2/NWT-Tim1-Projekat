package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.feign.DiscountFeignClient;
import the.convenient.foodie.restaurant.feign.OrderFeignClient;
import the.convenient.foodie.restaurant.dto.restaurant.FilterRestaurantRequest;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantShortResponse;
import the.convenient.foodie.restaurant.repository.custom.RestaurantRepositoryCustom;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DiscountFeignClient discountFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;




    @Override
    public List<RestaurantShortResponse> getRestaurants(FilterRestaurantRequest filters,String sortBy, Boolean ascending) {
        var hql = "SELECT new the.convenient.foodie.restaurant.dto.restaurant.RestaurantShortResponse(r,COALESCE(avg(rev.rating),0),count(rev.rating),count(fr.id))"
                + " FROM Restaurant r LEFT JOIN Review rev ON r.id=rev.restaurant.id LEFT JOIN FavoriteRestaurant fr ON r.id=fr.restaurant.id";

        var results = new ArrayList<RestaurantShortResponse>();
        var discountSuccessfullyCalled = false; //To check if discount service was called and returned an OK response

        Map<String, String> whereClauseMap = new HashMap<>();


        //Create where clause if name and/or category filters present
        if (filters != null) {
            if (filters.getName() != null && !filters.getName().isBlank()) {
                whereClauseMap.put("name", " LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) ");
            }

            if (filters.getCategoryIds() != null && !filters.getCategoryIds().isEmpty()) {
                whereClauseMap.put("category", " EXISTS(SELECT c FROM Category c " +
                        "WHERE c.id IN (:categoryIds) AND c MEMBER OF r.categories)");
            }

        }

        //Add where clause to HQL if name and/or category filters present
        if (!whereClauseMap.isEmpty()) {
            hql += " WHERE " + whereClauseMap.values().stream().collect(Collectors.joining(" AND "));
        }

        hql += " GROUP BY r";




        if (sortBy!=null && !sortBy.isEmpty()) {

            //ASCENDING order
            if (ascending) {

                //If sorting can be done in Restaurant DB, add ORDER clause to HQL
                switch (sortBy) {
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

                //Get filtered  (and sorted) results from query
                results.addAll(getResults(hql, whereClauseMap.containsKey("name") ? filters.getName() : null, whereClauseMap.containsKey("category") ? filters.getCategoryIds() : null));

                //If isOfferingDiscount filter is set, call Discount microservice and additionally filter the results
                if (filters != null && filters.getIsOfferingDiscount() != null && Boolean.valueOf(filters.getIsOfferingDiscount())) {

                    var res = filterRestaurantsBasedOnDiscount(results);

                    //If Discount microservice returned a 200 response, apply filter, otherwise ignore it
                    if(res!=null) {
                        discountSuccessfullyCalled=true; //this is to make sure if we have an empty result list that it is valid
                        results = (ArrayList<RestaurantShortResponse>) res;
                    }
                }

                //Call Order microservice to sort fetched results if necessary
                if (sortBy.equals("POPULARITY")) {
                    var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                    //Call Order Microservice
                    try {
                    var orderMap = orderFeignClient.getNumberOfOrdersPerRestaurant(resultUUIDs, "asc");
                    results.stream().sorted(Comparator.comparing(r -> orderMap.get(r.getUuid())));
                    } catch(Exception e) {
                        logger.error(e.getMessage());
                    }

                }




            } else {

                //DESCENDING sort
                switch (sortBy) {
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


                results.addAll(getResults(hql,whereClauseMap.containsKey("name") ? filters.getName() : null, whereClauseMap.containsKey("category") ? filters.getCategoryIds() : null));

                //If isOfferingDiscount filter is set, call Discount microservice and additionally filter the results
                if (filters != null && filters.getIsOfferingDiscount() != null && Boolean.valueOf(filters.getIsOfferingDiscount())) {

                    var res = filterRestaurantsBasedOnDiscount(results);

                    //If Discount microservice returned a 200 response, apply filter, otherwise ignore it
                    if(res!=null) {
                        discountSuccessfullyCalled=true; //this is to make sure if we have an empty result list that it is valid
                        results = (ArrayList<RestaurantShortResponse>) res;
                    }
                }

                if (sortBy.equals("POPULARITY")) {
                    var resultUUIDs = results.stream().map(r -> r.getUuid()).collect(Collectors.toList());
                    //Call Order Microservice
                    try {
                        var orderMap = orderFeignClient.getNumberOfOrdersPerRestaurant(resultUUIDs, "desc");
                        results.stream().sorted((r1, r2) -> orderMap.get(r2.getUuid()).compareTo(orderMap.get(r1.getUuid())));
                    } catch(Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }


        } else {

            //If sortBy is not set, use default sorting
            hql += " ORDER BY avg(rev.rating) DESC";

            results.addAll(getResults(hql, whereClauseMap.containsKey("name") ? filters.getName() : null, whereClauseMap.containsKey("category") ? filters.getCategoryIds() : null));

            //If isOfferingDiscount filter is set, call Discount microservice and additionally filter the results
            if (filters != null && filters.getIsOfferingDiscount() != null && Boolean.valueOf(filters.getIsOfferingDiscount())) {

                var res = filterRestaurantsBasedOnDiscount(results);

                //If Discount microservice returned a 200 response, apply filter, otherwise ignore it
                if(res!=null) {
                    discountSuccessfullyCalled=true; //this is to make sure if we have an empty result list that it is valid
                    results = (ArrayList<RestaurantShortResponse>) res;
                }
            }
        }



        return results;

    }

    @Override
    public String getRestaurantUUID(Long id) {
        var hql = "SELECT r.uuid FROM Restaurant r WHERE r.id=:id";
        try {
            return entityManager.createQuery(hql, String.class).setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    private List<RestaurantShortResponse> getResults(String hql,String name, List<Long> categoryIds) {
        logger.info("HQL: {}",hql);
        var query = entityManager.createQuery(hql, RestaurantShortResponse.class);

        if (name != null)
            query.setParameter("name", name);

        if (categoryIds != null)
            query.setParameter("categoryIds", categoryIds);



        return query.getResultList();
    }

    private List<RestaurantShortResponse> filterRestaurantsBasedOnDiscount(List<RestaurantShortResponse> restaurarantsToFilter) {
        //Call Discount MS

        List<String> uuids = restaurarantsToFilter.stream().map(r -> r.getUuid()).collect(Collectors.toList());
        try {
            List<String> res = discountFeignClient.filterDiscountedRestaurants(uuids);

            return (ArrayList<RestaurantShortResponse>) restaurarantsToFilter.stream().filter(r -> res.contains(r.getUuid())).collect(Collectors.toList());


        }  catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


}

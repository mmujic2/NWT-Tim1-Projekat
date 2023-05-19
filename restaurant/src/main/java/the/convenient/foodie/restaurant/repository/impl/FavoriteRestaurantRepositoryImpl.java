package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantShortResponse;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.custom.FavoriteRestaurantRepositoryCustom;

import java.sql.SQLException;
import java.util.List;

public class FavoriteRestaurantRepositoryImpl implements FavoriteRestaurantRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RestaurantShortResponse> getFavoriteRestaurants(String userUUID) {
        var hql = "SELECT new the.convenient.foodie.restaurant.dto.restaurant.RestaurantShortResponse(r,avg(rev.rating),count(rev.id),count(fr.id))  " +
                "from Restaurant r LEFT JOIN FavoriteRestaurant fr ON r.id = fr.restaurant.id LEFT JOIN Review"
                + " rev ON r.id=rev.restaurant.id WHERE fr.userUUID=:userUUID GROUP BY r";
        var query = entityManager.createQuery(hql, RestaurantShortResponse.class).setParameter("userUUID",userUUID);
        return query.getResultList();
    }

    @Override
    public void removeRestaurantFromFavorites(Long restaurantId, String userUUID){
        var hql = "DELETE from FavoriteRestaurant fr where fr.userUUID=:userUUID and"+
                " fr.restaurant.id=:restaurantId";
        var query = entityManager.createQuery(hql).setParameter("userUUID",userUUID).
                setParameter("restaurantId", restaurantId);
        query.executeUpdate();
    }
}

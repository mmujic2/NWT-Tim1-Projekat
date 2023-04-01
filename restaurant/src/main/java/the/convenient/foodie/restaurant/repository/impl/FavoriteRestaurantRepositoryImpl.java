package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.custom.FavoriteRestaurantRepositoryCustom;

import java.sql.SQLException;
import java.util.List;

public class FavoriteRestaurantRepositoryImpl implements FavoriteRestaurantRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurant> getFavoriteRestaurants(String userUUID) {
        var hql = "SELECT r from FavoriteRestaurant  fr,Restaurant r where fr.restaurant.id=r.id"
                + " and fr.userUUID=:userUUID";
        var query = entityManager.createQuery(hql, Restaurant.class).setParameter("userUUID",userUUID);
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

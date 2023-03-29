package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.custom.CategoryRepositoryCustom;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurant> getRestaurantsWithCategories(List<Long> categoryIds) {
        var hql = "SELECT r from Restaurant r, Category c where c.id in (:categoryIds)"
                + " and c member of r.categories";
        var query = entityManager.createQuery(hql, Restaurant.class).setParameter("categoryIds",categoryIds);
        return query.getResultList();
    }
}

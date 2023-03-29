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
    public List<Restaurant> getRestaurantsWithCategory(Long categoryId) {
        var hql = "SELECT r from Restaurant r, Category c where c.id = : categoryId"
                + " and c member of r.categories";
        var query = entityManager.createQuery(hql, Restaurant.class).setParameter("categoryId",categoryId);
        return query.getResultList();
    }
}

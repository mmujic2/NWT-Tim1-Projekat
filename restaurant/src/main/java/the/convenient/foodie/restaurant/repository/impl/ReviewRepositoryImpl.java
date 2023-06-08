package the.convenient.foodie.restaurant.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import the.convenient.foodie.restaurant.dto.review.ReviewResponse;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.model.Review;
import the.convenient.foodie.restaurant.repository.custom.ReviewRepositoryCustom;

import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReviewResponse> getReviewsForRestaurant(String restaurantUUID) {
        var hql = "SELECT new the.convenient.foodie.restaurant.dto.review.ReviewResponse(r.id,r.userUUID,r.restaurant.id,r.restaurant.uuid,r.comment,r.rating,r.created) from Review r  where r.restaurant.uuid = :restaurantUUID order by r.created desc";
        var query = entityManager.createQuery(hql, ReviewResponse.class).setParameter("restaurantUUID",restaurantUUID);
        return query.getResultList();
    }

    @Override
    public List<Review> getUserReviews(String userUUID) {
        var hql = "SELECT r from Review r  where r.userUUID = :userUUID order by r.created desc";
        var query = entityManager.createQuery(hql, Review.class).setParameter("userUUID",userUUID);
        return query.getResultList();
    }

    @Override
    public Double calculateAverageRatingForRestaurant(Long restaurantId) {
        var hql = "SELECT AVG(r.rating) from Review r  where r.restaurant.id = :restaurantId";
        var query = entityManager.createQuery(hql).setParameter("restaurantId",restaurantId);
        return (Double) query.getSingleResult();
    }
}

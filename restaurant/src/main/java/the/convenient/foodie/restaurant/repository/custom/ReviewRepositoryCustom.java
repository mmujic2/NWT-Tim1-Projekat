package the.convenient.foodie.restaurant.repository.custom;

import the.convenient.foodie.restaurant.dto.review.ReviewResponse;
import the.convenient.foodie.restaurant.model.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    public List<ReviewResponse> getReviewsForRestaurant(String restaurantUUID);

    public List<Review> getUserReviews(String userUUID);

    public Double calculateAverageRatingForRestaurant(Long restaurantId);
}

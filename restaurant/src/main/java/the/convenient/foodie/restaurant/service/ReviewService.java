package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dto.review.ReviewResponse;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;
import the.convenient.foodie.restaurant.repository.ReviewRepository;
import the.convenient.foodie.restaurant.dto.review.ReviewCreateRequest;
import the.convenient.foodie.restaurant.model.Review;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Review addNewReview(ReviewCreateRequest request) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUserUUID(request.getUserUUID());
        var exception = new EntityNotFoundException("Restaurant with id " + request.getRestaurantId() + " does not exist!");
        var restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(()-> exception);
        review.setRestaurant(restaurant);
        review.setCreated(LocalDateTime.now());
        //Update with userID/name
        review.setCreatedBy("test");
        reviewRepository.save(review);

        return review;
    }

    public List<ReviewResponse> getReviewsForRestaurant(String restaurantUUID) {
        var exception = new EntityNotFoundException("Restaurant with uuid " + restaurantUUID + " does not exist!");
        restaurantRepository.findByUUID(restaurantUUID).orElseThrow(()-> exception);
        return reviewRepository.getReviewsForRestaurant(restaurantUUID);
    }

    public List<Review> getUserReviews(String userUUID) {

        return reviewRepository.getUserReviews(userUUID);
    }


    public String deleteReview(Long id) {
        var review = reviewRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Review with id " + id + " does not exist!"));
        reviewRepository.delete(review);
        return "Review with id " + id + " successfully deleted!";
    }
}

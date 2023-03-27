package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dao.CategoryRepository;
import the.convenient.foodie.restaurant.dao.RestaurantRepository;
import the.convenient.foodie.restaurant.dao.ReviewRepository;
import the.convenient.foodie.restaurant.dao.dto.ReviewCreateRequest;
import the.convenient.foodie.restaurant.entity.Category;
import the.convenient.foodie.restaurant.entity.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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


    public String deleteReview(Long id) {
        var review = reviewRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Review with id " + id + " does not exist!"));
        reviewRepository.delete(review);
        return "Review with id " + id + " successfully deleted!";
    }
}

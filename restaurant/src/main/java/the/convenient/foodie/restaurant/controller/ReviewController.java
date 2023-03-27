package the.convenient.foodie.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dao.dto.RestaurantCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.RestaurantUpdateRequest;
import the.convenient.foodie.restaurant.dao.dto.ReviewCreateRequest;
import the.convenient.foodie.restaurant.entity.Restaurant;
import the.convenient.foodie.restaurant.entity.Review;
import the.convenient.foodie.restaurant.service.RestaurantService;
import the.convenient.foodie.restaurant.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping(path="/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @Operation(description = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new review",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Review> addNewReview (
            @Parameter(description = "Information required for review creation", required = true)
            @Valid @RequestBody ReviewCreateRequest request) {

        var review = reviewService.addNewReview(request);
        return new ResponseEntity<>(review,HttpStatus.CREATED);
    }

    @Operation(description = "Delete a review")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the review with provided ID"),
            @ApiResponse(responseCode = "404", description = "Review with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteReview(
            @Parameter(description = "Review ID", required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(reviewService.deleteReview(id),HttpStatus.OK);
    }



}

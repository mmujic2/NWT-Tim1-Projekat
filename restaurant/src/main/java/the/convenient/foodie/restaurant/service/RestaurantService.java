package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dto.*;
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;
import the.convenient.foodie.restaurant.model.OpeningHours;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Restaurant addNewRestaurant(RestaurantCreateRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setManagerUUID(request.getManagerUUID());
        restaurant.setCreated(LocalDateTime.now());
        //Update with userID/name
        restaurant.setCreatedBy("test");
        restaurantRepository.save(restaurant);

        return restaurant;
    }

    public Restaurant updateRestaurant(RestaurantUpdateRequest request,Long id) {
            var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
                var restaurant = restaurantRepository.findById(id).orElseThrow(()-> exception);
                restaurant.setName(request.getName());
                restaurant.setManagerUUID(request.getManagerUUID());
                restaurant.setMapCoordinates(request.getMapCoordinates());
                restaurant.setAddress(request.getAddress());
                restaurant.setModified(LocalDateTime.now());
                //Update with userID/name
                restaurant.setModifiedBy("test");
                restaurantRepository.save(restaurant);
                return restaurant;

    }

    public List<Restaurant> getAllRestaurants() {

        return StreamSupport.stream(restaurantRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public Page<RestaurantWithRating> searchForRestaurants(FilterRestaurantRequest filterRestaurantRequest, Pageable pageable) {

        return restaurantRepository.getRestaurants(filterRestaurantRequest,pageable);
    }

    public Restaurant getRestaurant(Long id) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id);
        return restaurant.orElseThrow(()-> exception);
    }

    public List<Restaurant> getRestaurantsWithCategories(List<Long> categoryIds) {

        return categoryRepository.getRestaurantsWithCategories(categoryIds);

    }

    public String deleteRestaurant(Long id) {
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Restaurant with id " + id + " does not exist!"));
        restaurantRepository.delete(restaurant);
        return "Restaurant with id " + id + " successfully deleted!";
    }

    public Restaurant addCategoriesToRestaurant(Long id, List<Long> categoryIds) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);
        var categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        restaurant.setCategories(categories);
        restaurant.setModified(LocalDateTime.now());
        //Update with userID/name
        restaurant.setModifiedBy("test");
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Restaurant setRestaurantOpeningHours(Long id, OpeningHoursCreateRequest request) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);

        //Update with user id/name
        var created = "test";
        var openingHours = new OpeningHours(request.getMondayOpen(),
                request.getMondayClose(),
                request.getTuesdayOpen(),
                request.getTuesdayClose(),
                request.getWednesdayOpen(),
                request.getWednesdayClose(),
                request.getThursdayOpen(),
                request.getThursdayClose(),
                request.getFridayOpen(),
                request.getFridayClose(),
                request.getSaturdayOpen(),
                request.getSaturdayClose(),
                request.getSundayOpen(),
                request.getSundayClose(),
                LocalDateTime.now(),
                created);

        restaurant.setOpeningHours(openingHours);
        restaurant.setModified(LocalDateTime.now());
        //Update with userID/name
        restaurant.setModifiedBy("test");
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Double calculateAverageRatingForRestaurant(Long restaurantId) {
        var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        restaurantRepository.findById(restaurantId).orElseThrow(()->exception);
        return reviewRepository.calculateAverageRatingForRestaurant(restaurantId);
    }
}

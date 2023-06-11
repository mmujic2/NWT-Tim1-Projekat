package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dto.openinghours.OpeningHoursCreateRequest;
import the.convenient.foodie.restaurant.dto.restaurant.*;
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.FavoriteRestaurantRepository;
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

    @Autowired
    private FavoriteRestaurantRepository favoriteRestaurantRepository;


    public Restaurant addNewRestaurant(RestaurantCreateRequest request, String username) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setManagerUUID(request.getManagerUUID());
        restaurant.setAddress(request.getAddress());
        restaurant.setMapCoordinates(request.getMapCoordinates());
        restaurant.setCreated(LocalDateTime.now());
        restaurant.setCreatedBy(username);
        restaurantRepository.save(restaurant);

        return restaurant;
    }

    public Restaurant updateRestaurant(RestaurantUpdateRequest request, Long id,String uuid) {
            var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
                var restaurant = restaurantRepository.findById(id).orElseThrow(()-> exception);
                restaurant.setName(request.getName());
                restaurant.setMapCoordinates(request.getMapCoordinates());
                restaurant.setAddress(request.getAddress());
                restaurant.setLogo(request.getLogo());
                restaurant.setModified(LocalDateTime.now());
                restaurant.setModifiedBy(uuid);
                restaurantRepository.save(restaurant);
                return restaurant;

    }


    public List<RestaurantShortResponse> searchForRestaurants(FilterRestaurantRequest filterRestaurantRequest,String sortBy, Boolean ascending) {

        return restaurantRepository.getRestaurants(filterRestaurantRequest,sortBy,ascending);
    }
    public List<RestaurantShortResponse> getFullRestaurants() {

        return restaurantRepository
                .getRestaurants(null,"DATE",false);
    }

    public RestaurantShortResponse getRestaurantById(Long id,String customerUUID) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantShortResponseById(id);
            System.out.println(restaurant);
            restaurant.setCustomerFavorite(restaurantRepository.checkIfRestaurantIsCustomersFavorite(id,customerUUID));
            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }

    }

    public RestaurantResponse getRestaurantByManagerUUID(String managerUUID) {
        var exception = new EntityNotFoundException("Restaurant with manager UUID " + managerUUID + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantByManagerUUID(managerUUID);
            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }

    }

    public String getRestaurantUUIDByManagerUUID(String managerUUID) {
        var exception = new EntityNotFoundException("Restaurant with manager UUID " + managerUUID + " does not exist!");
        try {
            return restaurantRepository.getRestaurantUUIDByManagerUUID(managerUUID);
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }

    }

    public RestaurantResponse getRestaurantFullResponseById(Long id) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        try {
            return restaurantRepository.getRestaurantFullResponseById(id);
        } catch(Exception e) {
            throw exception;
        }

    }

    public List<Restaurant> getRestaurantsWithCategories(List<Long> categoryIds) {

        return categoryRepository.getRestaurantsWithCategories(categoryIds);

    }

    public String deleteRestaurant(Long id) {
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Restaurant with id " + id + " does not exist!"));
        restaurantRepository.delete(restaurant);
        return "Restaurant with id " + id + " successfully deleted!";
    }

    public Restaurant addCategoriesToRestaurant(Long id, List<Long> categoryIds,String userUUID) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);
        if(restaurant.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList()).equals(categoryIds)) {
            System.out.println("all match");
            return restaurant;
        }
        var categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        restaurant.setCategories(categories);
        restaurant.setModified(LocalDateTime.now());
        restaurant.setModifiedBy(userUUID);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Restaurant setRestaurantOpeningHours(Long id, OpeningHoursCreateRequest request, String userUUID) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);


        var created = userUUID;
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
        restaurant.setModifiedBy(created);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Double calculateAverageRatingForRestaurant(Long restaurantId) {
        var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        restaurantRepository.findById(restaurantId).orElseThrow(()->exception);
        return reviewRepository.calculateAverageRatingForRestaurant(restaurantId);
    }

    public String getRestaurantUUID(Long id) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var uuid= restaurantRepository.getRestaurantUUID(id);
        if(uuid==null)
            throw exception;
        return uuid;
    }


    public Long getCustomersFavorited(String restaurantUUID) {
        restaurantRepository.findByUUID(restaurantUUID).orElseThrow();
        return favoriteRestaurantRepository.countNumberOfFavorites(restaurantUUID);
    }
}

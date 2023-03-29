package the.convenient.foodie.restaurant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import the.convenient.foodie.restaurant.model.*;
import the.convenient.foodie.restaurant.repository.*;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@EnableDiscoveryClient
@EntityScan(basePackages="the.convenient.foodie.restaurant.model")
@SpringBootApplication
public class RestaurantApplication implements CommandLineRunner {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    FavoriteRestaurantRepository favoriteRestaurantRepository;


	public static void main(String[] args) {

		SpringApplication.run(RestaurantApplication.class, args);

	}


    @Override
    public void run(String... args) throws Exception
    {
        //cleanDB();
        //dumpData();
    }

    private void cleanDB() {
        reviewRepository.deleteAll();
        favoriteRestaurantRepository.deleteAll();
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        openingHoursRepository.deleteAll();

    }
    private void dumpData() {
        var creationDate = LocalDateTime.now();
        var createdBy = "Dump";
        var categories = new ArrayList<Category>();
        categories.add(new Category("Traditional food",creationDate,createdBy));
        categories.add(new Category("Pizzeria",creationDate,createdBy));
        categories.add(new Category("Sweets",creationDate,createdBy));

        categoryRepository.saveAll(categories);
        var manager1 = UUIDGenerator.generateType1UUID().toString();
        var manager2 = UUIDGenerator.generateType1UUID().toString();
        var user = UUIDGenerator.generateType1UUID().toString();

        var timeOpen = LocalTime.of(9,0);
        var timeClose = LocalTime.of(22,0);
        var timeClose2 =LocalTime.of(20,0);
        var openingHours1 = new OpeningHours(timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose2,
                null,null, creationDate, createdBy);
        var openingHours2 = new OpeningHours(timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose,
                timeOpen,timeClose2,
                null,null, creationDate, createdBy);

        var restaurant1 = new Restaurant();
        restaurant1.setName("Ćevabdžinica Željo");
        restaurant1.setAddress("Kundurdžiluk 19, Sarajevo");
        restaurant1.setMapCoordinates("43.859915157258925, 18.4298921620989");
        restaurant1.setManagerUUID(manager1);
        List<Category> categories1 = new ArrayList<>();
        categories1.add(categories.get(0));
        restaurant1.setCategories(new HashSet<>(categories1));
        restaurant1.setOpeningHours(openingHours1);
        restaurant1.setCreated(creationDate);
        restaurant1.setCreatedBy(createdBy);

        var restaurant2 = new Restaurant();
        restaurant2.setName("Montana");
        restaurant2.setAddress("Bulevar Meše Selimovića 2c, Sarajevo");
        restaurant2.setMapCoordinates("43.85050510789651, 18.366089839082676");
        restaurant2.setManagerUUID(manager2);
        List<Category> categories2 = new ArrayList<>();
        categories2.add(categories.get(1));
        categories2.add(categories.get(2));
        restaurant2.setCategories(new HashSet<>(categories2));
        restaurant2.setOpeningHours(openingHours2);
        restaurant2.setCreated(creationDate);
        restaurant2.setCreatedBy(createdBy);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        restaurantRepository.saveAll(restaurants);

        var review = new Review();
        review.setRating(5);
        review.setUserUUID(user);
        review.setRestaurant(restaurant1);
        review.setCreated(creationDate);
        review.setCreatedBy(createdBy);

        reviewRepository.save(review);

        var favoriteRestaurant1 = new FavoriteRestaurant();
        favoriteRestaurant1.setUserUUID(user);
        favoriteRestaurant1.setRestaurant(restaurant1);
        favoriteRestaurant1.setCreated(creationDate);
        favoriteRestaurant1.setCreatedBy(createdBy);

        var favoriteRestaurant2 = new FavoriteRestaurant();
        favoriteRestaurant2.setUserUUID(user);
        favoriteRestaurant2.setRestaurant(restaurant2);
        favoriteRestaurant2.setCreated(creationDate);
        favoriteRestaurant2.setCreatedBy(createdBy);

        favoriteRestaurantRepository.save(favoriteRestaurant1);
        favoriteRestaurantRepository.save(favoriteRestaurant2);


    }

}

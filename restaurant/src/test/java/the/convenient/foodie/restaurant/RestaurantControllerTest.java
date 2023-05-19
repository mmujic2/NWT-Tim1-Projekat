package the.convenient.foodie.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import the.convenient.foodie.restaurant.config.JPAConfig;
import the.convenient.foodie.restaurant.model.Review;
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.FavoriteRestaurantRepository;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;
import the.convenient.foodie.restaurant.dto.openinghours.OpeningHoursCreateRequest;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantCreateRequest;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantUpdateRequest;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.ReviewRepository;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RestaurantApplication.class, JPAConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RestaurantControllerTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
    }

    @BeforeEach
    public void initialData() {
        var categories = new ArrayList<Category>();
        var category1 = new Category();
        var category2 = new Category();
        var category3 = new Category();
        category1.setName("Category 1");
        category1.setCreated(LocalDateTime.now());
        category1.setCreatedBy("test");
        category2.setName("Category 2");
        category2.setCreated(LocalDateTime.now());
        category2.setCreatedBy("test");
        category3.setName("Category 3");
        category3.setCreated(LocalDateTime.now());
        category3.setCreatedBy("test");

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        categoryRepository.saveAll(categories);

        categories.remove(category2);

        var listOfRestaurants = new ArrayList<Restaurant>();
        var restaurant1 = new Restaurant();
        restaurant1.setCreated(LocalDateTime.now());
        restaurant1.setCreatedBy("test");
        restaurant1.setUuid(UUIDGenerator.generateType1UUID().toString());
        restaurant1.setName("Restaurant 1");
        restaurant1.setManagerUUID(UUIDGenerator.generateType1UUID().toString());
        restaurant1.setCategories(new HashSet<>(categories));
        var restaurant2 = new Restaurant();
        restaurant2.setCreated(LocalDateTime.now());
        restaurant2.setCreatedBy("test");
        restaurant2.setUuid(UUIDGenerator.generateType1UUID().toString());
        restaurant2.setName("Restaurant 2");
        restaurant2.setManagerUUID(UUIDGenerator.generateType1UUID().toString());
        listOfRestaurants.add(restaurant1);
        listOfRestaurants.add(restaurant2);

        restaurantRepository.saveAll(listOfRestaurants);

        var reviews = new ArrayList<Review>();
        var review1 = new Review();
        review1.setRestaurant(restaurant1);
        review1.setCreated(LocalDateTime.now());
        review1.setCreatedBy("test");
        review1.setUserUUID("test");
        review1.setRating(5);
        var review2 = new Review();
        review2.setRestaurant(restaurant1);
        review2.setCreated(LocalDateTime.now());
        review2.setCreatedBy("test");
        review2.setUserUUID("test");
        review2.setRating(4);

        reviews.add(review1);
        reviews.add(review2);

        reviewRepository.saveAll(reviews);

        var favorites = new ArrayList<FavoriteRestaurant>();
        var fav1 = new FavoriteRestaurant();
        fav1.setCreated(LocalDateTime.now());
        fav1.setUserUUID("test");
        fav1.setRestaurant(restaurant2);
        fav1.setCreatedBy("test");

        favorites.add(fav1);

        favoriteRestaurantRepository.saveAll(favorites);
    }


    @AfterEach
    public void deleteData() {
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    public void getAllShouldReturnAllRestaurants() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/restaurant/all"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var restaurants = objectMapper.convertValue(objectMapper.readTree(content), Restaurant[].class);

        Assertions.assertEquals(2, restaurants.length);
    }

    @Test
    public void getByIdShouldReturnExistingRestaurant() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/restaurant/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var restaurant = objectMapper.convertValue(objectMapper.readTree(content), Restaurant.class);


        Assertions.assertEquals("Restaurant 1",restaurant.getName());
    }

    @Test
    public void getByIdShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/restaurant/-1"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id -1 does not exist!"));
    }

    @Test
    public void addShouldCreateNewRestaurantWhenInformationValid() throws Exception {
        RestaurantCreateRequest request = new RestaurantCreateRequest("New restaurant",UUIDGenerator.generateType1UUID().toString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                post("/restaurant/add")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var restaurant = objectMapper.convertValue(objectMapper.readTree(content), Restaurant.class);

        Assertions.assertEquals("New restaurant",restaurant.getName());
    }

    @Test
    public void addShouldNotCreateNewRestaurantWhenInformationInvalid() throws Exception {
        RestaurantCreateRequest request = new RestaurantCreateRequest("T","");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/restaurant/add")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant name must be between 3 and 80 characters!"));
        Assertions.assertTrue(content.contains("UUID must be 36 characters long!"));
    }

    @Test
    public void updateShouldUpdateRestaurantWhenInformationValid() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("Update restaurant","Address", "62.333,-76.56",UUIDGenerator.generateType1UUID().toString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/update/{id}",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var restaurant = objectMapper.convertValue(objectMapper.readTree(content), Restaurant.class);

        Assertions.assertEquals("Update restaurant",restaurant.getName());
    }

    @Test
    public void updateShouldNotUpdateRestaurantWhenInformationInvalid() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("Update restaurant","A", "862.333,-876.56",UUIDGenerator.generateType1UUID().toString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/update/{id}",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Map coordinates must represent latitude and longitude values!"));
        Assertions.assertTrue(content.contains("Restaurant address must be between 3 and 80 characters!"));
    }

    @Test
    public void deleteShouldDeleteExistingRestaurant() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/restaurant/{id}", id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id " + id + " successfully deleted!"));

    }

    @Test
    public void deleteShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/restaurant/{id}", -1))
                        .andExpect(status().is4xxClientError())
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id -1 does not exist!"));

    }

    @Test
    public void addCategoriesShouldUpdateRestaurantCategories() throws Exception {
        var categories = categoryRepository.findAll();
        var categoryIds = new ArrayList<Long>();
        categoryIds.add(categories.get(0).getId());
        categoryIds.add(categories.get(1).getId());

        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 2").findFirst().get().getId();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/{id}/add-categories",id)
                        .content(asJsonString(categoryIds))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var restaurant = objectMapper.convertValue(objectMapper.readTree(content), Restaurant.class);

        Assertions.assertTrue(restaurant.getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()).contains("Category 1"));
        Assertions.assertTrue(restaurant.getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()).contains("Category 2"));
    }

    @Test
    public void setOpeningHoursShouldUpdateRestaurantOpeningHoursWhenInformationValid() throws Exception {
        var open = LocalTime.of(9,0,0);
        var close = LocalTime.of(22,0,0);
        var request = new OpeningHoursCreateRequest(open,close,open,close,open,close,open,close,open,close,open,close,null,null);

        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/{id}/set-opening-hours",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var restaurant = objectMapper.convertValue(objectMapper.readTree(content), Restaurant.class);

        Assertions.assertEquals(open,restaurant.getOpeningHours().getMondayOpen());
        Assertions.assertEquals(close,restaurant.getOpeningHours().getMondayClose());
        Assertions.assertNull(restaurant.getOpeningHours().getSundayOpen());
        Assertions.assertNull(restaurant.getOpeningHours().getSundayClose());
    }

    @Test
    public void setOpeningHoursShouldNotUpdateRestaurantOpeningHoursWhenInformationInvalid() throws Exception {
        var open = LocalTime.of(9,0,0);
        var close1 = LocalTime.of(8,0,0);
        var close2 = LocalTime.of(9,45,0);
        var request = new OpeningHoursCreateRequest(open,close1,open,close1,open,close2,open,null,null,close1,null,null,null,null);

        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/{id}/set-opening-hours",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Both opening and closing hours for the same day must be specified!"));
        Assertions.assertTrue(content.contains("Closing hours can not be before opening hours on the same day!"));
        Assertions.assertTrue(content.contains("There must be at least an hour between opening and closing times!"));


    }

    @Test
    public void addRestaurantToFavoritesShouldCreateFavoriteRestaurantLinkForExistingRestaurant() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
       MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/restaurant/{id}/add-to-favorites",id)
                       .param("user","test"))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        var favoriteRestaurant = objectMapper.convertValue(objectMapper.readTree(content), FavoriteRestaurant.class);

        Assertions.assertEquals(id,favoriteRestaurant.getRestaurant().getId());
    }

    @Test
    public void addRestaurantToFavoritesShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/restaurant/-1/add-to-favorites")
                        .param("user","test"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id -1 does not exist!"));
    }

    @Test
    public void getRestaurantsWithCategoriesShouldReturnAllRestaurantsWithGivenCategories() throws Exception {
        var allCategories = categoryRepository.findAll();
        var categoryIds = allCategories.stream().filter(c->!c.getName().equals("Category 2")).map(c -> c.getId().toString()).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/category")
                .param("categoryIds", categoryIds))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var restaurants = objectMapper.convertValue(objectMapper.readTree(content), Restaurant[].class);

        Assertions.assertEquals(1, restaurants.length);
    }

    @Test
    public void getAverageRatingForRestaurantShouldReturnAverageRatingForExistingRestaurant() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/{id}/rating",id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var rating = objectMapper.convertValue(objectMapper.readTree(content), Double.class);

        Assertions.assertEquals(4.5, rating);
    }

    @Test
    public void getAverageRatingForRestaurantShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/{id}/rating",-1))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id -1 does not exist!"));
    }

    @Test
    public void getFavoriteRestaurantsShouldReturnFavoriteRestaurantsOfGivenUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/favorites")
                        .param("user","test"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var restaurants = objectMapper.convertValue(objectMapper.readTree(content),Restaurant[].class);

        Assertions.assertTrue(Arrays.stream(restaurants).map(r -> r.getName()).collect(Collectors.toList()).contains("Restaurant 2"));
    }

    @Test
    public void removeRestaurantFromFavoritesShouldDeleteFavoriteRestaurantLinkForExistingRestaurant() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 2").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/{id}/remove-from-favorites",id)
                        .param("user","user"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Successfully removed restaurant with id " + id + " from favorites!"));
    }

    @Test
    public void removeRestaurantFromFavoritesShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/restaurant/{id}/remove-from-favorites",-1)
                        .param("user","user"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Restaurant with id -1 does not exist!"));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

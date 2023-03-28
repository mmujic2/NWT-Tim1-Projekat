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
import the.convenient.foodie.restaurant.dao.CategoryRepository;
import the.convenient.foodie.restaurant.dao.RestaurantRepository;
import the.convenient.foodie.restaurant.dao.dto.OpeningHoursCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.RestaurantCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.RestaurantUpdateRequest;
import the.convenient.foodie.restaurant.entity.Category;
import the.convenient.foodie.restaurant.entity.FavoriteRestaurant;
import the.convenient.foodie.restaurant.entity.Restaurant;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
        category1.setName("Category 1");
        category1.setCreated(LocalDateTime.now());
        category1.setCreatedBy("test");
        category2.setName("Category 2");
        category2.setCreated(LocalDateTime.now());
        category2.setCreatedBy("test");

        categories.add(category1);
        categories.add(category2);

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

        Assertions.assertEquals("Restaurant with id -1 does not exist!", content);
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
                        post("/restaurant/{id}/add-to-favorites",id))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        var favoriteRestaurant = objectMapper.convertValue(objectMapper.readTree(content), FavoriteRestaurant.class);

        Assertions.assertEquals(id,favoriteRestaurant.getRestaurant().getId());
    }

    @Test
    public void addRestaurantToFavoritesShouldReturnErrorForNonExistingRestaurant() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/restaurant/-1/add-to-favorites"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("Restaurant with id -1 does not exist!",content);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

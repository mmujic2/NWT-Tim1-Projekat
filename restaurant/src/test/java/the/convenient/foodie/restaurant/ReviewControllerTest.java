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
import the.convenient.foodie.restaurant.dao.ReviewRepository;
import the.convenient.foodie.restaurant.dao.dto.CategoryCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.ReviewCreateRequest;
import the.convenient.foodie.restaurant.entity.Category;
import the.convenient.foodie.restaurant.entity.Restaurant;
import the.convenient.foodie.restaurant.entity.Review;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RestaurantApplication.class, JPAConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReviewControllerTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

        var restaurant1 = new Restaurant();
        restaurant1.setCreated(LocalDateTime.now());
        restaurant1.setCreatedBy("test");
        restaurant1.setName("Restaurant 1");
        restaurant1.setManagerUUID(UUIDGenerator.generateType1UUID().toString());

        restaurantRepository.save(restaurant1);

        var review = new Review();
        review.setRating(5);
        review.setUserUUID(UUIDGenerator.generateType1UUID().toString());
        review.setCreatedBy("test");
        review.setCreated(LocalDateTime.now());
        review.setRestaurant(restaurant1);

        reviewRepository.save(review);
    }


    @AfterEach
    public void deleteData() {
        reviewRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    public void addShouldCreateNewReviewWhenInformationValid() throws Exception {
        var allRestaurants = restaurantRepository.findAll();
        var id = allRestaurants.stream().filter(r->r.getName()=="Restaurant 1").findFirst().get().getId();
        ReviewCreateRequest request = new ReviewCreateRequest("Comment",3,UUIDGenerator.generateType1UUID().toString(),id);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/review/add")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var category = objectMapper.convertValue(objectMapper.readTree(content), Review.class);

        Assertions.assertEquals("Comment",category.getComment());
    }

    @Test
    public void addShouldNotCreateNewReviewWhenInformationInvalid() throws Exception {
        ReviewCreateRequest request = new ReviewCreateRequest("T",3,UUIDGenerator.generateType1UUID().toString(),1L);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/review/add")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Comment must be between 2 and 100 characters!"));
    }

    @Test
    public void deleteShouldDeleteExistingReview() throws Exception {
        var allReviews = reviewRepository.findAll();
        var id = allReviews.stream().filter(r->r.getRating()==5).findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/review/{id}", id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Review with id " + id + " successfully deleted!"));

    }

    @Test
    public void deleteShouldReturnErrorForNonExistingReview() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/review/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Review with id -1 does not exist!"));

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

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
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;
import the.convenient.foodie.restaurant.dto.CategoryCreateRequest;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RestaurantApplication.class, JPAConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryControllerTest {

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

        var restaurant1 = new Restaurant();
        restaurant1.setCreated(LocalDateTime.now());
        restaurant1.setCreatedBy("test");
        restaurant1.setUuid(UUIDGenerator.generateType1UUID().toString());
        restaurant1.setName("Restaurant 1");
        restaurant1.setManagerUUID(UUIDGenerator.generateType1UUID().toString());
        restaurant1.setCategories(new HashSet<>(categories));

        restaurantRepository.save(restaurant1);
    }


    @AfterEach
    public void deleteData() {
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void getAllShouldReturnAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/category/all"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var categories = objectMapper.convertValue(objectMapper.readTree(content), Category[].class);

        Assertions.assertEquals(2, categories.length);
    }

    @Test
    public void getByIdShouldReturnExistingCategory() throws Exception {
        var allCategories = categoryRepository.findAll();
        var id = allCategories.stream().filter(c->c.getName()=="Category 1").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/category/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var category = objectMapper.convertValue(objectMapper.readTree(content), Category.class);


        Assertions.assertEquals("Category 1",category.getName());
    }

    @Test
    public void getByIdShouldReturnErrorForNonExistingCategory() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/category/-1"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("Category with id -1 does not exist!", content);
    }

    @Test
    public void addShouldCreateNewCategoryWhenInformationValid() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("New category");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/category/add")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        var category = objectMapper.convertValue(objectMapper.readTree(content), Category.class);

        Assertions.assertEquals("New category",category.getName());
    }

    @Test
    public void addShouldNotCreateNewCategoryWhenInformationInvalid() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/category/add")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Category name must be between 3 and 80 characters!"));
    }

    @Test
    public void updateShouldUpdateCategoryWhenInformationValid() throws Exception {
        var allCategories = categoryRepository.findAll();
        var id = allCategories.stream().filter(c->c.getName()=="Category 1").findFirst().get().getId();
        CategoryCreateRequest request = new CategoryCreateRequest("Update category");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/category/update/{id}",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var category = objectMapper.convertValue(objectMapper.readTree(content), Category.class);

        Assertions.assertEquals("Update category",category.getName());
    }

    @Test
    public void updateShouldNotUpdateCategoryWhenInformationInvalid() throws Exception {
        var allCategories = categoryRepository.findAll();
        var id = allCategories.stream().filter(c->c.getName()=="Category 1").findFirst().get().getId();
        CategoryCreateRequest request = new CategoryCreateRequest("");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        put("/category/update/{id}",id)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Category name must be between 3 and 80 characters!"));
    }

    @Test
    public void deleteShouldDeleteExistingCategory() throws Exception {
        var allCategories = categoryRepository.findAll();
        var id = allCategories.stream().filter(c->c.getName()=="Category 1").findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/category/{id}", id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Category with id " + id + " successfully deleted!"));

    }

    @Test
    public void deleteShouldReturnErrorForNonExistingCategory() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        delete("/category/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Category with id -1 does not exist!"));

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

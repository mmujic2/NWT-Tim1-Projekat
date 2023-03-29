package the.convenient.foodie.menu;

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
import the.convenient.foodie.menu.config.JPAConfig;
import the.convenient.foodie.menu.dao.MenuItemRepository;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.dto.MenuDto;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.model.MenuItem;
import the.convenient.foodie.menu.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MenuApplication.class, JPAConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

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
        Menu menu = new Menu();
        menu.setActive(true);
        menu.setDate_created(LocalDateTime.now());
        menu.setRestaurant_uuid(UUIDGenerator.generateType1UUID().toString());
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Ćevapi petica");
        menuItem.setDescription("Pet ćevapa u pola somuna");
        menuItem.setPrice(4.0);
        menuItem.setDiscount_price(null);
        menuItem.setPrep_time(5.0);
        menuItem.setDate_created(LocalDateTime.now());
        var itemi = new ArrayList<MenuItem>();
        itemi.add(menuItem);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("Ćevapi desetka");
        menuItem2.setDescription("Deset ćevapa u pola somuna");
        menuItem2.setPrice(7.0);
        menuItem2.setDiscount_price(null);
        menuItem2.setPrep_time(5.0);
        menuItem2.setDate_created(LocalDateTime.now());
        itemi.add(menuItem2);
        menu.setMenuItems(itemi);
        menuRepository.save(menu);
    }

    @AfterEach
    public void deleteData() {
        menuRepository.deleteAll();
    }

    @Test
    public void getAllShouldReturnAllMenus() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/menu/all"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var menus = objectMapper.convertValue(objectMapper.readTree(content), Menu[].class);

        Assertions.assertEquals(1, menus.length);
    }

    @Test
    public void addNewMenuValid() throws Exception {
        var restaurant_uuid = UUIDGenerator.generateType1UUID().toString();
        MenuDto menuDao = new MenuDto(true, restaurant_uuid);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/menu/add")
                        .content(asJsonString(menuDao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var menu = objectMapper.convertValue(objectMapper.readTree(content), Menu.class);

        Assertions.assertEquals(restaurant_uuid,menu.getRestaurant_uuid());
    }

    @Test
    public void getMenuByIDValid() throws Exception {
        var allMenus = menuRepository.findAll();
        var id = allMenus.stream().filter(r->r.isActive()==true).findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/menu/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var menu = objectMapper.convertValue(objectMapper.readTree(content), Menu.class);

        Assertions.assertNotNull(menu);
    }

    @Test
    public void getMenuByIDInvalid() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/menu/2"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("Menu with id 2 does not exist!", content);
    }

    @Test
    public void addNewMenuInvalid() throws Exception {
        var restaurant_uuid = "123";
        MenuDto menuDao = new MenuDto(true, restaurant_uuid);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        post("/menu/add")
                        .content(asJsonString(menuDao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("UUID must be 36 characters long!"));
    }



    @Test
    public void test() {
        Assertions.assertEquals(2,2);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

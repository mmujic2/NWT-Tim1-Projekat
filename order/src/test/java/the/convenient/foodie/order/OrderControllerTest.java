package the.convenient.foodie.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import the.convenient.foodie.order.exception.MenuItemNotFoundException;
import the.convenient.foodie.order.model.MenuItem;
import the.convenient.foodie.order.model.Order;
import the.convenient.foodie.order.repository.MenuItemRepository;
import the.convenient.foodie.order.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {OrderApplication.class, JpaConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
    }

    public List<MenuItem> InitMenuItems() {
        List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem("Baklava", "Veoma lijepo", 3.0, 3.0, null, 30));
        menuItemList.add(new MenuItem("Burek", "300g, dobar", 2.0, 1.5, null, 5));
        menuItemList.add(new MenuItem("Grah", "400ml, sa suhim mesom", 6.5, 6.0, null, 5));

        menuItemRepository.saveAll(menuItemList);
        return menuItemRepository.findAll();
    }

    @BeforeEach
    public void loadData() {
        // Ovdje napuniti lokalnu bazu sa testnim podacima
        var menuItemList = InitMenuItems();

        List<Order> orderItemList = new ArrayList<>();
        try {
            orderItemList.add(new Order(1L, 1L, 30, LocalDateTime.now(), 1L, "Fresh", 25.5, null, 3.5, "somecode",
                    new ArrayList<>(List.of(menuItemList.get(0).getId(), menuItemList.get(2).getId()))));
            orderItemList.add(new Order(2L, 1L, 25, LocalDateTime.now(), null, "In delivery", 25.5, 1L, 3.5, null,
                    new ArrayList<>(List.of(menuItemList.get(1).getId()))));
            orderItemList.add(new Order(3L, 2L, 10, LocalDateTime.now(), null, "Delivered", 25.5, null, 3.5, null,
                    new ArrayList<>(List.of(menuItemList.get(0).getId(), menuItemList.get(1).getId(), menuItemList.get(2).getId()))));

        } catch (MenuItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        orderRepository.saveAll(orderItemList);
    }

    @AfterEach
    public void deleteData() {
        orderRepository.deleteAll();
        menuItemRepository.deleteAll();
    }

    @Test
    public void OrderGetTest() throws Exception {
        // Za testiranjem koristi se MockMvc
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/get")).andReturn();
        // Get tijelo response-a
        String content = result.getResponse().getContentAsString();

        // Koristi se objectmapper da bi se rekreirao objekat iz json-a, ne mora se ovo koristiti, moze se sve kroz
        // kroz mockmvc objekat ali ovako je brze i ljepse

        var orders = objectMapper.convertValue(objectMapper.readTree(content), Order[].class);
        // Moze se koristiti Junit ili jupyter
        Assertions.assertEquals(3, orders.length);
        for(var order : orders) {
           Assertions.assertNotNull(order);
        }
    }

    @Test
    public void OrderGetByIdTest() throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/order/get/1")).andReturn();
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/order/get/2")).andReturn();

        var order1 = objectMapper.convertValue(objectMapper.readTree(result1.getResponse().getContentAsString()), Order.class);
        var order2 = objectMapper.convertValue(objectMapper.readTree(result2.getResponse().getContentAsString()), Order.class);

        Assertions.assertEquals(order1.getId().longValue(), 1L);
        Assertions.assertEquals(order2.getId().longValue(), 2L);
        Assertions.assertEquals(order1.getOrderStatus(), "Fresh");
        Assertions.assertEquals(order2.getOrderStatus(), "In delivery");
    }
}

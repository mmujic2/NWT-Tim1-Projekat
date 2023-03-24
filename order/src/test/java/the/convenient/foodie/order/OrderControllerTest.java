package the.convenient.foodie.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OrderApplication.class, JpaConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerTest {
    /*@Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;*/

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MockMvc mockMvc;

    public void InitMenuItems() {
        var newMenuItem = new MenuItem("Baklava", "Veoma lijepo", 3.0, 3.0, null, 30);
        newMenuItem.setId(1L);
        menuItemRepository.saveAndFlush(newMenuItem);
    }
    @Before
    public void setUp() {
        InitMenuItems();
        var menuItems = new ArrayList<Long>(); menuItems.add(1L);
        Order newOrder = null;
        try {
            newOrder = new Order(1L,
                    1L,
                    10,
                    LocalDateTime.now(),
                    1L,
                    "Fresh",
                    25.5,
                    1L,
                    3.5,
                    "nope",
                    menuItems);
        } catch (MenuItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        orderRepository.saveAndFlush(newOrder);
    }

    @Test
    public void OrderGetTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/get")).andReturn();
        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        var order = objectMapper.treeToValue(objectMapper.readTree(content).get(0), Order.class);
        Assert.assertEquals(order.getOrderStatus(), "Fresh");
    }
}

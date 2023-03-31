package the.convenient.foodie.discount;

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
import the.convenient.foodie.discount.config.JPAConfig;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.dto.CouponDto;
import the.convenient.foodie.discount.model.Coupon;
import the.convenient.foodie.discount.util.UUIDGenerator;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {DiscountApplication.class, JPAConfig.class})
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private CouponRepository couponRepository;

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
        Coupon c = new Coupon("kod111111111",5,1,20);
        c.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());

        Coupon c2 = new Coupon("kod22222222",5,2,25);
        c2.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
        couponRepository.save(c2);
    }

    @AfterEach
    public void deleteData() {
        couponRepository.deleteAll();
    }

    @Test
    public void getAllCouponsTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/all"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var coupons = objectMapper.convertValue(objectMapper.readTree(content), Coupon[].class);

        Assertions.assertEquals(2, coupons.length);
    }

    @Test
    public void addNewCouponTest() throws Exception {
        var coupon_uuid = UUIDGenerator.generateType1UUID().toString();
        CouponDto menuDao = new CouponDto("kod55555555", 10, 8, 10);
        menuDao.setCoupon_uuid(coupon_uuid);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                post("/coupon/add")
                .content(asJsonString(menuDao))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        var coupon = objectMapper.convertValue(objectMapper.readTree(content), Coupon.class);

        Assertions.assertEquals(coupon_uuid,coupon.getCoupon_uuid());
    }

    @Test
    public void getMenuByIDTest() throws Exception {
        var coupons = couponRepository.findAll();
        var id = coupons.stream().filter(r->r.getRestaurant_id()==2).findFirst().get().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        var coupon = objectMapper.convertValue(objectMapper.readTree(content), Coupon.class);

        Assertions.assertNotNull(coupon);
    }

    @Test
    public void getMenuByIDTest2() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/2"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("Coupon with id 2 does not exist!", content);
    }

    @Test
    public void addNewMenuInvalid() throws Exception {
        var coupon_uuid = "123";
        CouponDto couponDto = new CouponDto("kod66666666", 10, 28, 10);
        couponDto.setCoupon_uuid(coupon_uuid);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                post("/coupon/add")
                .content(asJsonString(couponDto))
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

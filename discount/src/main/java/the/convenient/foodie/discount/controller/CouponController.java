package the.convenient.foodie.discount.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.entity.Coupon;

@Controller // This means that this class is a Controller
@RequestMapping(path="/coupon") // This means URL's start with /demo (after Application path)
public class CouponController {
    private CouponRepository couponRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewCoupon (@RequestParam String code, @RequestParam Integer quantity, @RequestParam Integer restaurant_id,@RequestParam Integer discount_percentage,@RequestParam String coupon_uuid) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Coupon n = new Coupon(code,quantity,restaurant_id,discount_percentage,coupon_uuid);
        couponRepository.save(n);
        return "Added coupon";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Coupon> getAllCoupons() {
        // This returns a JSON or XML with the users
        return couponRepository.findAll();
    }
}

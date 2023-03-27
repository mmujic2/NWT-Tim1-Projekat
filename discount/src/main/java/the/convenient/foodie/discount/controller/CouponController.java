package the.convenient.foodie.discount.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.entity.Coupon;

@RestController
@RequestMapping(path="/coupon") // This means URL's start with /demo (after Application path)
public class CouponController {
    private CouponRepository couponRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody
    ResponseEntity<Coupon> addNewCoupon (@RequestParam String code, @RequestParam Integer quantity, @RequestParam Integer restaurant_id, @RequestParam Integer discount_percentage, @RequestParam String coupon_uuid) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Coupon n = new Coupon(code,quantity,restaurant_id,discount_percentage,coupon_uuid);
        couponRepository.save(n);
        return new ResponseEntity<>(n,HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Coupon> getAllCoupons() {
        // This returns a JSON or XML with the users
        return couponRepository.findAll();
    }

    @GetMapping(path="/test")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String vrati() {
        // This returns a JSON or XML with the users
        return "Proba";
    }
}

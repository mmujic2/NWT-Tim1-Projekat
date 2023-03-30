package the.convenient.foodie.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dao.CouponDto;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.service.CouponService;

import java.util.List;

@RestController
@RequestMapping(path="/coupon") // This means URL's start with /demo (after Application path)
public class CouponController {
    private CouponService couponService;

    @Operation(description = "Get all coupons")
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<Coupon>> getAllCoupons() {
        var coupons = couponService.getAllCoupons();
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @Operation(description = "Get a coupon by coupon ID")
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Coupon> getCoupon(@PathVariable  Integer id) {
        var coupon = couponService.getCoupon(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Operation(description = "Create a new coupon")
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Coupon> addNewMenu(@Valid @RequestBody CouponDto couponDto) {
        var coupon = couponService.addNewCoupon(couponDto);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @Operation(description = "Update coupon information")
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Coupon> updateMenu(@PathVariable Integer id, @Valid @RequestBody CouponDto couponDto){
        var coupon = couponService.updateCoupon(couponDto, id);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a coupon")
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteMenu(@PathVariable Integer id) {
        return new ResponseEntity<>(couponService.deleteCoupon(id), HttpStatus.OK);
    }

}

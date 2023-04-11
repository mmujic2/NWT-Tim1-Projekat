package the.convenient.foodie.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import the.convenient.foodie.discount.dto.CouponDto;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.service.CouponService;

import java.util.List;

@RestController
@Validated
@RequestMapping(path="/coupon") // This means URL's start with /demo (after Application path)
public class CouponController {
    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    private CouponService couponService;

    @Operation(description = "Get all coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all coupons",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) })}
    )
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<Coupon>> getAllCoupons() {
        var coupons = couponService.getAllCoupons();
        //dont make a loop by accident like I did
        //String response = restTemplate.getForObject("http://order-service/order/get", String.class);
        //System.out.println(response);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @Operation(description = "Get a coupon by coupon ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupon with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Coupon> getCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable  Integer id) {
        var coupon = couponService.getCoupon(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Operation(description = "Create a new coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Coupon> addNewCoupon(@Parameter(description = "Information required for coupon creation", required = true) @Valid @RequestBody CouponDto couponDto) {
        var coupon = couponService.addNewCoupon(couponDto);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @Operation(description = "Update coupon information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated coupon information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Coupon> updateCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable Integer id, @Parameter(description = "Coupon information to be updated", required = true) @Valid @RequestBody CouponDto couponDto){
        var coupon = couponService.updateCoupon(couponDto, id);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a coupon")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the coupon with provided ID"),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable Integer id) {
        return new ResponseEntity<>(couponService.deleteCoupon(id), HttpStatus.OK);
    }

    @Operation(description = "Filter restaurants with coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found filtered restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) })}
    )
    @PostMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<String>> filterRestaurants(@Parameter(description = "Restaurant UUID list", required = true) @RequestBody List<String> restaurants) {
        var filteredRestaurants = couponService.filterRestaurants(restaurants);
        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }

}

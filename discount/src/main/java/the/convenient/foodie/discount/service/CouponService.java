package the.convenient.foodie.discount.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.discount.dto.CouponDto;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.entity.Coupon;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return StreamSupport.stream(couponRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Coupon getCoupon(Integer id) {
        var exception = new EntityNotFoundException("Coupon with id " + id + " does not exist!");
        var coupon = couponRepository.findById(id);
        return coupon.orElseThrow(() -> exception);
    }

    public Coupon addNewCoupon(CouponDto couponDto) {
        Coupon coupon = new Coupon(couponDto);
        couponRepository.save(coupon);
        return coupon;
    }

    public String deleteCoupon(Integer id) {
        var coupon = couponRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Coupon with id " + id + " does not exist!"));
        couponRepository.deleteById(id);
        return "Coupon with id " + id + " is successfully deleted!";
    }

    public Coupon updateCoupon(CouponDto couponDto, Integer id) {
        var exception = new EntityNotFoundException("Coupon with id " + id + " does not exist!");
        var coupon = couponRepository.findById(id).orElseThrow(() -> exception);
        coupon.setCode(couponDto.getCode());
        coupon.setCoupon_uuid(couponDto.getCoupon_uuid());
        coupon.setQuantity(couponDto.getQuantity());
        coupon.setDiscount_percentage(couponDto.getDiscount_percentage());
        coupon.setRestaurant_id(couponDto.getRestaurant_id());
        couponRepository.save(coupon);
        return coupon;
    }


}

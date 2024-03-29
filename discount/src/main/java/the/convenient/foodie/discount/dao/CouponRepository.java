package the.convenient.foodie.discount.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.discount.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
}

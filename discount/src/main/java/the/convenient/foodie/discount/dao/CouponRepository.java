package the.convenient.foodie.discount.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.discount.entity.Coupon;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, Integer> {
}

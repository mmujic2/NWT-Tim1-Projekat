package the.convenient.foodie.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.dao.RequiredScoreRepository;
import the.convenient.foodie.discount.dao.ScoreRepository;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.entity.RequiredScore;
import the.convenient.foodie.discount.entity.Score;
import the.convenient.foodie.discount.util.UUIDGenerator;

//@EnableJpaRepositories("the.convenient.foodie.discount.dao")
@EnableDiscoveryClient(autoRegister = true)
@EntityScan(basePackages = "the.convenient.foodie.discount.entity")
@SpringBootApplication
public class DiscountApplication implements CommandLineRunner {

	@Autowired
	CouponRepository couponRepository;
	@Autowired
	ScoreRepository scoreRepository;
	@Autowired
	RequiredScoreRepository requiredScoreRepository;

	public static void main(String[] args) {
		SpringApplication.run(DiscountApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception
	{
		cleanup();
		startingData();
	}

	private void cleanup() {
		couponRepository.deleteAll();
		scoreRepository.deleteAll();
		requiredScoreRepository.deleteAll();
	}

	private void startingData() {
		Coupon c = new Coupon("kod121314156",5,"132",20);
		c.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
		Coupon c2 = new Coupon("lol121314156",5,"154",20);
		c2.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
		Coupon c3 = new Coupon("kol121314156",4,"333",10);
		c2.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
		Coupon c4 = new Coupon("fol121314156",52,"154",20);
		c4.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
		Coupon amila = new Coupon("amila1314156",5,"f26a2606-0186-1000-9f09-422e554c87ec",20);
		amila.setCoupon_uuid(UUIDGenerator.generateType1UUID().toString());
		couponRepository.save(c);
		couponRepository.save(c2);
		couponRepository.save(c3);
		couponRepository.save(c4);
		couponRepository.save(amila);
		Score s = new Score(341,10,3150);
		scoreRepository.save(s);
		RequiredScore r = new RequiredScore(31,500);
		requiredScoreRepository.save(r);
	}

}

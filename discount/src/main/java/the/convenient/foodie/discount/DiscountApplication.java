package the.convenient.foodie.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.dao.RequiredScoreRepository;
import the.convenient.foodie.discount.dao.ScoreRepository;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.entity.RequiredScore;
import the.convenient.foodie.discount.entity.Score;

@EnableJpaRepositories("the.convenient.foodie.discount.dao")
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
		Coupon c = new Coupon("kod1213",5,132,20,"uuidkod1213");
		couponRepository.save(c);
		Score s = new Score(341,10,3150);
		scoreRepository.save(s);
		RequiredScore r = new RequiredScore(31,500);
		requiredScoreRepository.save(r);
	}

}

package the.convenient.foodie.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import the.convenient.foodie.discount.dao.CouponRepository;
import the.convenient.foodie.discount.entity.Coupon;

@EnableJpaRepositories("the.convenient.foodie.discount.dao")
@EntityScan(basePackages = "the.convenient.foodie.discount.entity")
@SpringBootApplication
public class DiscountApplication implements CommandLineRunner {

	@Autowired
	CouponRepository couponRepository;

	public static void main(String[] args) {
		SpringApplication.run(DiscountApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception
	{
		dumpData();
	}

	private void dumpData() {
		Coupon c = new Coupon("kod1",3,2,10,"uuidkod1");
		couponRepository.save(c);
	}

}

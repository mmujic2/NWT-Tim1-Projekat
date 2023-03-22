package the.convenient.foodie.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.entity.Menu;

import java.time.LocalDateTime;

@EnableJpaRepositories("the.convenient.foodie.menu.dao")
@EntityScan(basePackages = "the.convenient.foodie.menu.entity")
@SpringBootApplication
public class MenuApplication implements CommandLineRunner {

	@Autowired
	MenuRepository menuRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		dumpData();
	}

	private void dumpData() {
		Menu menu = new Menu();
		menu.setActive(true);
		menu.setRestaurant_uuid(null);
		menu.setDate_created(LocalDateTime.now());
		menuRepository.save(menu);
	}
}

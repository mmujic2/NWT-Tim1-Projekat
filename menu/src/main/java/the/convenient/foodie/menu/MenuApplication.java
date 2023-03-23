package the.convenient.foodie.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import the.convenient.foodie.menu.dao.MenuItemRepository;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.entity.Menu;
import the.convenient.foodie.menu.entity.MenuItem;
import the.convenient.foodie.menu.util.UUIDGenerator;

import java.time.LocalDateTime;

@EnableJpaRepositories("the.convenient.foodie.menu.dao")
@EntityScan(basePackages = "the.convenient.foodie.menu.entity")
@SpringBootApplication
public class MenuApplication implements CommandLineRunner {

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	MenuItemRepository menuItemRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		cleanDB();
		dumpData();
	}

	private void cleanDB() {
		menuItemRepository.deleteAll();
		menuRepository.deleteAll();
	}

	private void dumpData() {
		Menu menu = new Menu();
		menu.setActive(true);
		menu.setRestaurant_uuid(null);
		menu.setDate_created(LocalDateTime.now());
		menuRepository.save(menu);
		MenuItem menuItem = new MenuItem();
		menuItem.setMenu(menu);
		menuItem.setName("Ćevapi petica");
		menuItem.setDescription("Pet ćevapa u pola somuna");
		menuItem.setPrice(4.0);
		menuItem.setDiscount_price(null);
		menuItem.setPrep_time(5.0);
		menuItem.setUuid(UUIDGenerator.generateType1UUID().toString());
		menuItem.setDate_created(LocalDateTime.now());
		menuItemRepository.save(menuItem);
		MenuItem menuItem2 = new MenuItem();
		menuItem2.setMenu(menu);
		menuItem2.setName("Ćevapi desetka");
		menuItem2.setDescription("Deset ćevapa u pola somuna");
		menuItem2.setPrice(7.0);
		menuItem2.setDiscount_price(null);
		menuItem2.setPrep_time(5.0);
		menuItem2.setUuid(UUIDGenerator.generateType1UUID().toString());
		menuItem2.setDate_created(LocalDateTime.now());
		menuItemRepository.save(menuItem2);

	}
}
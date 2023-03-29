package the.convenient.foodie.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import the.convenient.foodie.menu.dao.MenuItemRepository;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.model.MenuItem;
import the.convenient.foodie.menu.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;

@EntityScan(basePackages = "the.convenient.foodie.menu.model")
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
		menu.setRestaurant_uuid(UUIDGenerator.generateType1UUID().toString());
		MenuItem menuItem = new MenuItem();
		menuItem.setName("Ćevapi petica");
		menuItem.setDescription("Pet ćevapa u pola somuna");
		menuItem.setPrice(4.0);
		menuItem.setDiscount_price(null);
		menuItem.setPrep_time(5.0);
		menuItem.setDate_created(LocalDateTime.now());
		var itemi = new ArrayList<MenuItem>();
		itemi.add(menuItem);


		MenuItem menuItem2 = new MenuItem();
		menuItem2.setName("Ćevapi desetka");
		menuItem2.setDescription("Deset ćevapa u pola somuna");
		menuItem2.setPrice(7.0);
		menuItem2.setDiscount_price(null);
		menuItem2.setPrep_time(5.0);
		menuItem2.setDate_created(LocalDateTime.now());
		itemi.add(menuItem2);

		menu.setMenuItems(itemi);
		menuRepository.save(menu);

	}
}

package the.convenient.foodie.menu.repository.custom;

import the.convenient.foodie.menu.model.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> getMenusForRestaurant(String restaurantUUID);
    List<Menu> getActiveMenusForRestaurant(String restaurantUUID);
}

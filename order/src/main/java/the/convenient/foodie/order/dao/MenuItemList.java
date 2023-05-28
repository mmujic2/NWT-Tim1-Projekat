package the.convenient.foodie.order.dao;

import the.convenient.foodie.order.model.MenuItem;

import java.util.List;

public class MenuItemList {
    List<MenuItem> menuItems;

    public MenuItemList(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

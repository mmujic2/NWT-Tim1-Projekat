package the.convenient.foodie.menu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.menu.dao.MenuItemRepository;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.dao.dto.MenuDto;
import the.convenient.foodie.menu.dao.dto.MenuItemDto;
import the.convenient.foodie.menu.entity.Menu;
import the.convenient.foodie.menu.entity.MenuItem;
import the.convenient.foodie.menu.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<Menu> getAllMenus() {
        return StreamSupport.stream(menuRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Menu getMenu(Long id) {
        var exception = new EntityNotFoundException("Menu with id " + id + " does not exist!");
        var menu = menuRepository.findById(id);
        return menu.orElseThrow(() -> exception);
    }

    public Menu addNewMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        menu.setActive(menuDto.isActive());
        menu.setRestaurant_uuid(menuDto.getRestaurant_uuid());
        menu.setDate_created(LocalDateTime.now());
        menuRepository.save(menu);
        return menu;
    }

    public String deleteMenu(Long id) {
        var menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu with id " + id + " does not exist!"));
        menuRepository.deleteById(id);
        return "Menu with id " + id + " is successfully deleted!";
    }

    public Menu updateMenu(MenuDto menuDto, Long id) {
        var exception = new EntityNotFoundException("Menu with id " + id + " does not exist!");
        var menu = menuRepository.findById(id).orElseThrow(() -> exception);
        menu.setActive(menuDto.isActive());
        menu.setRestaurant_uuid(menuDto.getRestaurant_uuid());
        menu.setDate_modified(LocalDateTime.now());
        menuRepository.save(menu);
        return menu;
    }

    public Menu addMenuItemsToMenu(Long id, List<MenuItemDto> menuItemsDao) {
        var exception = new EntityNotFoundException("Menu with id " + id + " does not exist!");
        var menu = menuRepository.findById(id).orElseThrow(()->exception);

        if(menu.getMenuItems() == null)
            menu.setMenuItems(new ArrayList<>());

        var items = menu.getMenuItems();

        for (var menuItemDao: menuItemsDao) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDao.getName());
            menuItem.setDescription(menuItemDao.getDescription());
            menuItem.setPrice(menuItemDao.getPrice());
            menuItem.setPrep_time(menuItemDao.getPrep_time());
            menuItem.setDiscount_price(menuItemDao.getDiscount_price());
            //menuItem.setUuid(UUIDGenerator.generateType1UUID().toString());
            menuItem.setDate_created(LocalDateTime.now());
            items.add(menuItem);
        }
        menu.setMenuItems(items);
        menu.setDate_modified(LocalDateTime.now());
        menuRepository.save(menu);
        return menu;
    }

}

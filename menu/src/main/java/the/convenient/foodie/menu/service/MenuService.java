package the.convenient.foodie.menu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.menu.repository.MenuItemRepository;
import the.convenient.foodie.menu.repository.MenuRepository;
import the.convenient.foodie.menu.dto.MenuDto;
import the.convenient.foodie.menu.dto.MenuItemDto;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.model.MenuItem;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        menu.setName(menuDto.getName());
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
        menu.setName(menuDto.getName());
        menuRepository.save(menu);
        return menu;
    }

    public Menu addMenuItemsToMenu(Long id, List<MenuItemDto> menuItemsDao) {
        var exception = new EntityNotFoundException("Menu with id " + id + " does not exist!");
        var menu = menuRepository.findById(id).orElseThrow(()->exception);

        if(menu.getMenuItems() == null)
            menu.setMenuItems(new ArrayList<>());

        var items = menu.getMenuItems();
        var newItems = new ArrayList<MenuItem>();

        for (var menuItemDao: menuItemsDao) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDao.getName());
            menuItem.setDescription(menuItemDao.getDescription());
            menuItem.setPrice(menuItemDao.getPrice());
            menuItem.setPrep_time(menuItemDao.getPrep_time());
            menuItem.setDiscount_price(menuItemDao.getDiscount_price());
            //menuItem.setUuid(UUIDGenerator.generateType1UUID().toString());
            menuItem.setDate_created(LocalDateTime.now());
            menuItem.setImage(menuItemDao.getImage());
            items.add(menuItem);
            newItems.add(menuItem);
        }
        menu.setMenuItems(items);
        menu.setDate_modified(LocalDateTime.now());
        menuRepository.save(menu);

        try {
            List<Long> idList = new ArrayList<>();
            for(var item : newItems) idList.add(item.getId());

            var newItemsWithUUID = menuItemRepository.findAllById(idList);
            var objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new ParameterNamesModule());
            rabbitTemplate.convertAndSend("menuItemCreate", objectMapper.writeValueAsString(newItemsWithUUID));
        } catch (Exception e) {
            System.out.println("Something went wrong when fetching items");
        }
        return menu;
    }

    public List<MenuDto> getRestaurantMenusShort(String restaurantUUID) {
        return menuRepository.getMenusForRestaurantShort(restaurantUUID);
    }

    public List<Menu> getRestaurantMenus(String restaurantUUID) {
        return menuRepository.getMenusForRestaurant(restaurantUUID);
    }

    public List<Menu> getActiveRestaurantMenus(String restaurantUUID) {
        return menuRepository.getActiveMenusForRestaurant(restaurantUUID);
    }
}

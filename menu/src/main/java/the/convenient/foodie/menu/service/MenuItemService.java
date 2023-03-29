package the.convenient.foodie.menu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.menu.dao.MenuItemRepository;
import the.convenient.foodie.menu.dto.MenuItemDto;
import the.convenient.foodie.menu.model.MenuItem;

import java.time.LocalDateTime;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    public String deleteMenuItem(Long id) {
        var menu = menuItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu with id " + id + " does not exist!"));
        menuItemRepository.deleteById(id);
        return "Menu Item with id " + id + " is successfully deleted!";
    }

    public MenuItem updateMenuItem(MenuItemDto menuItemDto, Long id) {
        var exception = new EntityNotFoundException("Menu Item with id " + id + " does not exist!");
        var menuItem = menuItemRepository.findById(id).orElseThrow(() -> exception);
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setDiscount_price(menuItemDto.getDiscount_price());
        menuItem.setPrep_time(menuItemDto.getPrep_time());
        menuItem.setDate_modified(LocalDateTime.now());
        menuItemRepository.save(menuItem);
        return menuItem;
    }

    public MenuItem getMenuItem(Long id) {
        var exception = new EntityNotFoundException("Menu Item with id " + id + " does not exist!");
        var menuItem = menuItemRepository.findById(id);
        return menuItem.orElseThrow(() -> exception);
    }

}

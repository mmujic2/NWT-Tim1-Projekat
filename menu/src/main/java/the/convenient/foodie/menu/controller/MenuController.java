package the.convenient.foodie.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import the.convenient.foodie.menu.dao.MenuRepository;
import the.convenient.foodie.menu.entity.Menu;

@Controller
@RequestMapping(path = "menu")
public class MenuController {
    @Autowired
    private MenuRepository menuRepository;

    @GetMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
}

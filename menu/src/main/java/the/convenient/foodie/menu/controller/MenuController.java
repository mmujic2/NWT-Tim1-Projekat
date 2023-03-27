package the.convenient.foodie.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.menu.dao.dto.MenuDto;
import the.convenient.foodie.menu.entity.Menu;
import the.convenient.foodie.menu.service.MenuService;

import java.util.List;

@RestController
@RequestMapping(path = "menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Operation(description = "Get all menus")
    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<List<Menu>> getAllMenus() {
        var menus = menuService.getAllMenus();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Operation(description = "Get a menu by menu ID")
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Menu> getMenu(@PathVariable  Long id) {
        var menu = menuService.getMenu(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @Operation(description = "Create a new menu")
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Menu> addNewMenu(@Valid @RequestBody MenuDto menuDto) {
        var menu = menuService.addNewMenu(menuDto);
        return  new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @Operation(description = "Update menu informations")
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Menu> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuDto menuDto){
        var menu = menuService.updateMenu(menuDto, id);
        return  new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a menu")
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        return new ResponseEntity<>(menuService.deleteMenu(id), HttpStatus.OK);
    }
}

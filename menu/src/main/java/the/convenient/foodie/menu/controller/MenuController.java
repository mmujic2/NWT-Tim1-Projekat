package the.convenient.foodie.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import the.convenient.foodie.menu.dto.MenuDto;
import the.convenient.foodie.menu.dto.MenuItemDto;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.service.MenuService;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = "menu")
public class MenuController {

    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    private MenuService menuService;

    @Operation(description = "Get all menus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all menus",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)) })}
    )
    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<List<Menu>> getAllMenus() {
        var menus = menuService.getAllMenus();
       // String response = restTemplate.getForObject("http://discount-service/coupon/all", String.class);
        //System.out.println(response);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Operation(description = "Get a menu by menu ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the menu with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Menu> getMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable  Long id) {
        var menu = menuService.getMenu(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @Operation(description = "Create a new menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new menu",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Menu> addNewMenu(
            @Parameter(description = "Information required for menu creation", required = true)
            @Valid @RequestBody MenuDto menuDto) {
        var menu = menuService.addNewMenu(menuDto);
        return  new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @Operation(description = "Update menu informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Menu> updateMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Menu information to be updated", required = true)
            @Valid @RequestBody MenuDto menuDto){
        var menu = menuService.updateMenu(menuDto, id);
        return  new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a menu")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the menu with provided ID"),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(menuService.deleteMenu(id), HttpStatus.OK);
    }

    @Operation(description = "Set menu items for menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu items",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/set-menu-items")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Menu> setMenuItemsForMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Values of menu items", required = true)
            @RequestBody List<@Valid MenuItemDto> menuItemDtos) {
        var menu = menuService.addMenuItemsToMenu(id, menuItemDtos);
        return  new ResponseEntity<>(menu,HttpStatus.OK);
    }
}

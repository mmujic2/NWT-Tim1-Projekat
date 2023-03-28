package the.convenient.foodie.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.order.exception.MenuItemNotFoundException;
import the.convenient.foodie.order.model.MenuItem;
import the.convenient.foodie.order.repository.MenuItemRepository;

@RestController
@RequestMapping(path="/menuitem")
public class MenuItemController {
    private final MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /*@PostMapping(path = "/add")
    @ResponseBody
    public String addNewMenuItem(@Valid @RequestBody MenuItem menuItem)
    {
        menuItemRepository.save(menuItem);
        return "Menu item successfully created!";
    }*/

    private MenuItem applyPatchToOrder(JsonPatch patch, MenuItem targetMenuItem) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        JsonNode patched = patch.apply(objectMapper.convertValue(targetMenuItem, JsonNode.class));
        return objectMapper.treeToValue(patched, MenuItem.class);
    }

    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully patched menu item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuItem.class))}),
            @ApiResponse( responseCode = "304", description = "Invalid patch path", content = @Content),
            @ApiResponse( responseCode = "400", description = "Invalid patch", content = @Content),
            @ApiResponse( responseCode = "404", description = "Order not found", content = @Content)
    })
    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MenuItem> UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonPatch patch) throws MenuItemNotFoundException {
        var menuItem = menuItemRepository.findById(id).orElseThrow(MenuItemNotFoundException::new);
        try {
            var menuItemPatched = applyPatchToOrder(patch, menuItem);
            menuItemRepository.save(menuItemPatched);
            return new ResponseEntity<>(menuItemPatched, HttpStatus.OK);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

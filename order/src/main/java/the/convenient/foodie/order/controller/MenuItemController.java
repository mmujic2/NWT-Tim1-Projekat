package the.convenient.foodie.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.order.exception.MenuItemNotFoundException;
import the.convenient.foodie.order.model.MenuItem;
import the.convenient.foodie.order.model.Order;
import the.convenient.foodie.order.repository.MenuItemRepository;

@RestController
@RequestMapping(path="/menuitem")
public class MenuItemController {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostMapping(path = "/add")
    @ResponseBody
    public String addNewMenuItem(@Valid @RequestBody MenuItem menuItem)
    {
        menuItemRepository.save(menuItem);
        return "Menu item successfully created!";
    }

    private MenuItem applyPatchToOrder(JsonPatch patch, MenuItem targetMenuItem) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        JsonNode patched = patch.apply(objectMapper.convertValue(targetMenuItem, JsonNode.class));
        return objectMapper.treeToValue(patched, MenuItem.class);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    @ResponseBody
    public String UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonPatch patch) throws MenuItemNotFoundException {
        var menuItem = menuItemRepository.findById(id).orElseThrow(MenuItemNotFoundException::new);
        try {
            var menuItemPatched = applyPatchToOrder(patch, menuItem);
            menuItemRepository.save(menuItemPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "Menu item successfully updated!";
    }
}

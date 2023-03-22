package the.convenient.foodie.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/menuitem")
public class MenuItemController {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewMenuItem(@RequestParam String name,
                                               @RequestParam String desc,
                                               @RequestParam Double price,
                                               @RequestParam Double discountPrice,
                                               @RequestParam String image,
                                               @RequestParam Integer prepTime)
    {
        menuItemRepository.save(new MenuItem(name, desc, price, discountPrice,
                null, prepTime));

        return "Ok";
    }


}

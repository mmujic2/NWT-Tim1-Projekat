package the.convenient.foodie.discount.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dao.RequiredScoreRepository;
import the.convenient.foodie.discount.entity.RequiredScore;

@RestController
@RequestMapping(path="/requiredscore") // This means URL's start with /demo (after Application path)
public class RequiredScoreController {
    private RequiredScoreRepository requiredScoreRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRequiredScore (@RequestParam Integer orders_required, @RequestParam Integer money_required) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        RequiredScore r = new RequiredScore(orders_required,money_required);
        requiredScoreRepository.save(r);

        return "Added required score";
    }

    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<RequiredScore> getAllRequiredScores() {
        // This returns a JSON or XML with the users
        return requiredScoreRepository.findAll();
    }
}

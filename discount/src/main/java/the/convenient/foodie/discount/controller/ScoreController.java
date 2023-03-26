package the.convenient.foodie.discount.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dao.ScoreRepository;
import the.convenient.foodie.discount.entity.Score;

@RestController
@RequestMapping(path="/score") // This means URL's start with /demo (after Application path)
public class ScoreController {
    private ScoreRepository scoreRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewScore (@RequestParam Integer user_id, @RequestParam Integer number_of_orders, @RequestParam Integer money_spent) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Score s = new Score(user_id,number_of_orders,money_spent);
        scoreRepository.save(s);
        return "Added score";
    }

    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Score> getAllScores() {
        // This returns a JSON or XML with the users
        return scoreRepository.findAll();
    }
}

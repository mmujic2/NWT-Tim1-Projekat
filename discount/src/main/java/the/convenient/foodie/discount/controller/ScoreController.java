package the.convenient.foodie.discount.controller;

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
import the.convenient.foodie.discount.dto.ScoreDto;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.entity.Score;

import the.convenient.foodie.discount.service.ScoreService;

import java.util.List;

@RestController
@Validated
@RequestMapping(path="/score") // This means URL's start with /demo (after Application path)
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @Operation(description = "Get all scores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all scores",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Score.class)) })}
    )
    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<List<Score>> getAllScores() {
        var scores = scoreService.getAllScores();
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @Operation(description = "Get a score by score ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the score for provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Score.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Score for provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Score> getScore(@Parameter(description = "Score ID", required = true) @PathVariable  Integer id) {
        var score = scoreService.getScore(id);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }

    @Operation(description = "Create a new score")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new score",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Score.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Score> addNewScore(@Parameter(description = "Information required for score creation", required = true) @Valid @RequestBody ScoreDto scoreDto) {
        var score = scoreService.addNewScore(scoreDto);
        return  new ResponseEntity<>(score, HttpStatus.CREATED);
    }

    @Operation(description = "Update score information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated score information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Score.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Score for provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Score> updateScore(@Parameter(description = "Score ID", required = true) @PathVariable Integer id, @Parameter(description = "Score information to be updated", required = true) @Valid @RequestBody ScoreDto scoreDto){
        var score = scoreService.updateScore(scoreDto, id);
        return  new ResponseEntity<>(score, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a score")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the score for provided ID"),
            @ApiResponse(responseCode = "404", description = "Score for provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteScore(@Parameter(description = "Score ID", required = true) @PathVariable Integer id) {
        return new ResponseEntity<>(scoreService.deleteScore(id), HttpStatus.OK);
    }

    @GetMapping("/get/uuid")
    public @ResponseBody ResponseEntity<Score> getCouponByUserUUID(@RequestHeader("uuid") String uuid) {
        return ResponseEntity.ok(scoreService.getScoreByUserId(uuid));
    }

    @PostMapping("/update/incrementorders/{ordercount}")
    public @ResponseBody ResponseEntity<Score> incrementUserOrders(@RequestHeader("uuid") String uuid, @PathVariable Integer ordercount) {
        return ResponseEntity.ok(scoreService.incrementUserOrders(uuid, ordercount));
    }

    @PostMapping("/update/incrementmoney/{money}")
    public @ResponseBody ResponseEntity<Score> incrementUserMoneySpent(@RequestHeader("uuid") String uuid, @PathVariable Integer money) {
        return ResponseEntity.ok(scoreService.incrementUserMoneySpent(uuid, money));
    }
}

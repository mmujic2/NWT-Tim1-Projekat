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
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.discount.dto.RequiredScoreDto;
import the.convenient.foodie.discount.entity.RequiredScore;
import the.convenient.foodie.discount.service.RequiredScoreService;

import java.util.List;

@RestController
@RequestMapping(path="/requiredscore") // This means URL's start with /demo (after Application path)
public class RequiredScoreController {

    @Autowired
    private RequiredScoreService requiredScoreService;

    @Operation(description = "Get all required scores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all required scores",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequiredScore.class)) })}
    )
    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<List<RequiredScore>> getAllRequiredScores() {
        var requiredScores = requiredScoreService.getAllRequiredScores();
        return new ResponseEntity<>(requiredScores, HttpStatus.OK);
    }

    @Operation(description = "Get the required score for required score ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the required score for provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequiredScore.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Required score for provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<RequiredScore> getRequiredScore(@Parameter(description = "Required score ID", required = true) @PathVariable  Integer id) {
        var requiredScore = requiredScoreService.getRequiredScore(id);
        return new ResponseEntity<>(requiredScore, HttpStatus.OK);
    }

    @Operation(description = "Create a new required score")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new required score",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequiredScore.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<RequiredScore> addNewRequiredScore(@Parameter(description = "Information required for required score creation", required = true) @Valid @RequestBody RequiredScoreDto requiredScoreDto) {
        var requiredScore = requiredScoreService.addNewRequiredScore(requiredScoreDto);
        return  new ResponseEntity<>(requiredScore, HttpStatus.CREATED);
    }

    @Operation(description = "Update required score information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated required score information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequiredScore.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Required score for provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<RequiredScore> updateRequiredScore(@Parameter(description = "Required score ID", required = true) @PathVariable Integer id, @Parameter(description = "Required score information to be updated", required = true) @Valid @RequestBody RequiredScoreDto requiredScoreDto){
        var requiredScore = requiredScoreService.updateRequiredScore(requiredScoreDto, id);
        return  new ResponseEntity<>(requiredScore, HttpStatus.CREATED);
    }

    @Operation(description = "Delete a required score")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the required score for provided ID"),
            @ApiResponse(responseCode = "404", description = "Required score for provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteRequiredScore(@Parameter(description = "Required score ID", required = true) @PathVariable Integer id) {
        return new ResponseEntity<>(requiredScoreService.deleteRequiredScore(id), HttpStatus.OK);
    }
}

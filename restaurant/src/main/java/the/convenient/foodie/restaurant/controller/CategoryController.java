package the.convenient.foodie.restaurant.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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
import the.convenient.foodie.restaurant.dto.CategoryCreateRequest;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(description = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Category> addNewCategory (
            @Parameter(description = "Category name", required = true)
            @Valid @RequestBody CategoryCreateRequest request) {

        var category = categoryService.addNewCategory(request.getName());
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        com.example.demo.EventServiceGrpc.EventServiceBlockingStub stub = com.example.demo.EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("POST")
                .setEvent("Created a category " + request.getName()).setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }

    @Operation(description = "Update category name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated category name",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Category> updateCategory (
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Category name", required = true)
            @RequestBody @Valid CategoryCreateRequest request) {

        Category category = null;
        category = categoryService.updateCategory(request.getName(),id);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        com.example.demo.EventServiceGrpc.EventServiceBlockingStub stub = com.example.demo.EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Updated category " + request.getName()).setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @Operation(description = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all categories in the system",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) })}
    )
    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Category>> getAllCategories() {

        var categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(description = "Get a category by ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the category with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)})
    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Category> getCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable  Long id) {
        var category = categoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }



    @Operation(description = "Delete a category")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the category with provided ID"),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        com.example.demo.EventServiceGrpc.EventServiceBlockingStub stub = com.example.demo.EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("DELETE")
                .setEvent("Deleted a category").setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(categoryService.deleteCategory(id),HttpStatus.OK);
    }
}

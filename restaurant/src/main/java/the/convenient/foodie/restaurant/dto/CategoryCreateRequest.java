package the.convenient.foodie.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class CategoryCreateRequest implements Serializable {
    @NotNull(message = "Category name must be specified!")
    @Size(min=3,max=80,message = "Category name must be between 3 and 80 characters!")
    private String name;

    public CategoryCreateRequest(String name) {
        this.name = name;
    }

    public CategoryCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dto.category.CategoryCreateRequest;
import the.convenient.foodie.restaurant.repository.CategoryRepository;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    public Category addNewCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCreated(LocalDateTime.now());
        category.setCreatedBy(request.getUserUUID());
        categoryRepository.save(category);

        return category;
    }

    public Category updateCategory(CategoryCreateRequest request, Long id) {
        var exception = new EntityNotFoundException("Category with id " + id + " does not exist!");
        var category = categoryRepository.findById(id).orElseThrow(()-> exception);
        category.setName(request.getName());
        category.setModified(LocalDateTime.now());
        category.setModifiedBy(request.getUserUUID());
        categoryRepository.save(category);
        return category;
    }

    public List<Category> getAllCategories() {

        return StreamSupport.stream(categoryRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public Category getCategory(Long id) {
        var exception = new EntityNotFoundException("Category with id " + id + " does not exist!");
        var category = categoryRepository.findById(id);
        return category.orElseThrow(()-> exception);
    }



    public String deleteCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id " + id + " does not exist!"));
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        var restaurantsWithCategory = categoryRepository.getRestaurantsWithCategories(idList);
        restaurantsWithCategory.forEach( r ->
        {
            r.setCategories(new HashSet<>(r.getCategories().stream().filter(c -> c.getId()!=id).collect(Collectors.toList())));
            restaurantRepository.save(r);
        });
        categoryRepository.delete(category);
        return "Category with id " + id + " successfully deleted!";
    }
}

package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dao.CategoryRepository;
import the.convenient.foodie.restaurant.entity.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public Category addNewCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setCreated(LocalDateTime.now());
        //Update with userID/name
        category.setCreatedBy("test");
        categoryRepository.save(category);

        return category;
    }

    public Category updateCategory(String name, Long id) {
        var exception = new EntityNotFoundException("Category with id " + id + " does not exist!");
        var category = categoryRepository.findById(id).orElseThrow(()-> exception);
        category.setName(name);
        category.setModified(LocalDateTime.now());
        //Update with userID/name
        category.setModifiedBy("test");
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
        //Find all restaurants with this category and remove this category from list of categories
        var category = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id " + id + " does not exist!"));
        categoryRepository.delete(category);
        return "Category with id " + id + " successfully deleted!";
    }
}

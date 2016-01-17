package ru.todoo.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.todoo.domain.dto.CategoryDTO;
import ru.todoo.service.CategoryService;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 23.11.2015.
 */
@RestController
@RequestMapping(value = "/api/categories")
public class Category {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDTO> get() {
        return categoryService.readAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public CategoryDTO create(CategoryDTO category) {
        return categoryService.create(category);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        categoryService.delete(categoryId);
        return "{\"message\" : \"Category is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(CategoryDTO category) {
        categoryService.update(category);
        return "{\"message\" : \"Category is updated\"}";
    }

    @ExceptionHandler
    public String handleException(Exception e) {
        return "{\"message\" : \"" + e.getLocalizedMessage() + "\"}";
    }
}

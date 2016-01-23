package ru.todoo.web.api;

import org.springframework.web.bind.annotation.*;
import ru.todoo.service.CategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 23.11.2015.
 */
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ru.todoo.domain.dto.Category> get() {
        return categoryService.readAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ru.todoo.domain.dto.Category create(@RequestBody ru.todoo.domain.dto.Category category) {
        return categoryService.create(category);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        categoryService.delete(categoryId);
        return "{\"message\" : \"Category is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestBody ru.todoo.domain.dto.Category category) {
        categoryService.update(category);
        return "{\"message\" : \"Category is updated\"}";
    }
}

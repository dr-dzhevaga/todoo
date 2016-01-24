package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.CategoryDAO;
import ru.todoo.domain.dto.Category;
import ru.todoo.domain.entity.CategoryEntity;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
public class CategoryService {
    @Resource
    private CategoryDAO categoryDAO;

    @Resource
    private Mapper mapper;

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Category create(Category category) {
        CategoryEntity categoryEntity = mapper.map(category, CategoryEntity.class);
        categoryEntity = categoryDAO.create(categoryEntity);
        category = mapper.map(categoryEntity, Category.class);
        return category;
    }

    @Transactional(readOnly = true)
    public Category read(Integer id) {
        CategoryEntity categoryEntity = categoryDAO.read(id);
        Category category = mapper.map(categoryEntity, Category.class);
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> readAll() {
        List<CategoryEntity> categoryEntities = categoryDAO.readAll();
        List<Category> categories = new ArrayList<>(categoryEntities.size());
        mapper.map(categoryEntities, categories);
        return categories;
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public void delete(Integer id) {
        categoryDAO.delete(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public void update(Category category) {
        CategoryEntity categoryEntity = mapper.map(category, CategoryEntity.class);
        categoryDAO.update(categoryEntity);
    }
}

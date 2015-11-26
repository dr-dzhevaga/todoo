package ru.todoo.service;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Category;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class CategoryService {
    private final DerbyDAOHelper<CategoryDAO> daoHelper = new DerbyDAOHelper<>(CategoryDAO.class);

    public void addCategory(String name) throws PersistException {
        Category category = new Category();
        category.setName(name);
        daoHelper.execute(categoryDAO -> categoryDAO.create(category));
    }

    public Category getCategory(Integer categoryId) throws PersistException {
        return daoHelper.read(categoryDAO -> categoryDAO.read(categoryId));
    }

    public List<Category> getAllCategories() throws PersistException {
        return daoHelper.read(GenericDAO::readAll);
    }

    public void deleteCategory(Integer categoryId) throws PersistException {
        daoHelper.execute(categoryDAO -> categoryDAO.delete(categoryId));
    }
}

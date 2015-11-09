package ru.todoo.service;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class CategoryService {
    private final DerbyDAOHelper<CategoryDAO> daoHelper = new DerbyDAOHelper<>(CategoryDAO.class);

    public void addCategory(Category category) throws PersistException, SQLException {
        daoHelper.execute(categoryDAO -> categoryDAO.create(category));
    }

    public Category getCategory(Integer categoryId) throws PersistException, SQLException {
        return daoHelper.read(categoryDAO -> categoryDAO.read(categoryId));
    }

    public List<Category> getAllCategories() throws PersistException, SQLException {
        return daoHelper.read(GenericDAO::readAll);
    }

    public void deleteCategory(Integer categoryId) throws PersistException, SQLException {
        daoHelper.execute(categoryDAO -> categoryDAO.delete(categoryId));
    }
}

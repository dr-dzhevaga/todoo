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
    private final DerbyDAOHelper<CategoryDAO> daoHelper;

    public CategoryService() throws PersistException {
        daoHelper = new DerbyDAOHelper<>(CategoryDAO.class);
    }

    public Category create(Category category) throws PersistException {
        return daoHelper.callOnDAO(categoryDAO -> categoryDAO.create(category));
    }

    public Category read(Integer categoryId) throws PersistException {
        return daoHelper.callOnDAO(categoryDAO -> categoryDAO.read(categoryId));
    }

    public List<Category> readAll() throws PersistException {
        return daoHelper.callOnDAO(GenericDAO::readAll);
    }

    public void delete(Integer categoryId) throws PersistException {
        daoHelper.executeOnDAO(categoryDAO -> categoryDAO.delete(categoryId));
    }

    public void update(Category category) throws PersistException {
        daoHelper.executeOnDAO(categoryDAO -> categoryDAO.update(category));
    }
}

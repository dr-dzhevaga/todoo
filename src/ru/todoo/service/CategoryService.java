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
    public Category create(Category category) throws PersistException {
        return DAOHelper.callOnDAO(CategoryDAO.class, false, categoryDAO -> categoryDAO.create(category));
    }

    public Category read(Integer categoryId) throws PersistException {
        return DAOHelper.callOnDAO(CategoryDAO.class, false, categoryDAO -> categoryDAO.read(categoryId));
    }

    public List<Category> readAll() throws PersistException {
        return DAOHelper.callOnDAO(CategoryDAO.class, false, GenericDAO::readAll);
    }

    public void delete(Integer categoryId) throws PersistException {
        DAOHelper.executeOnDAO(CategoryDAO.class, false, categoryDAO -> categoryDAO.delete(categoryId));
    }

    public void update(Category category) throws PersistException {
        DAOHelper.executeOnDAO(CategoryDAO.class, false, categoryDAO -> categoryDAO.update(category));
    }
}

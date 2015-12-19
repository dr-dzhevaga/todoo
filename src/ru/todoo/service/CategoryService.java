package ru.todoo.service;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Category;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class CategoryService {
    public Category create(Category category) throws PersistException {
        return DAOProvider.getDAOUtil().callOnDAO(CategoryDAO.class, categoryDAO -> categoryDAO.create(category));
    }

    public Category read(Integer categoryId) throws PersistException {
        return DAOProvider.getDAOUtil().callOnDAO(CategoryDAO.class, categoryDAO -> categoryDAO.read(categoryId));
    }

    public List<Category> readAll() throws PersistException {
        return DAOProvider.getDAOUtil().callOnDAO(CategoryDAO.class, GenericDAO::readAll);
    }

    public void delete(Integer categoryId) throws PersistException {
        DAOProvider.getDAOUtil().executeOnDAO(CategoryDAO.class, categoryDAO -> categoryDAO.delete(categoryId));
    }

    public void update(Category category) throws PersistException {
        DAOProvider.getDAOUtil().executeOnDAO(CategoryDAO.class, categoryDAO -> categoryDAO.update(category));
    }
}

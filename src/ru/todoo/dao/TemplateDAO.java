package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Template;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public interface TemplateDAO extends GenericDAO<Template, Integer> {
    List<Template> readByCategory(Integer categoryId) throws PersistException;

    List<Template> readPopular() throws PersistException;
}

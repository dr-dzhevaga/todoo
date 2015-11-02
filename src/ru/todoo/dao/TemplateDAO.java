package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Template;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface TemplateDAO extends GenericDAO<Template, Integer> {
    List<Template> getByCategoryId(Integer categoryId) throws PersistException;

    List<Template> getPopular() throws PersistException;
}

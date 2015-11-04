package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Template;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface TemplateDAO extends GenericDAO<Template, Integer> {
    List<Template> readAllRoot() throws PersistException;
    List<Template> readPopularRoot() throws PersistException;
    List<Template> readRootByCategory(Integer categoryId) throws PersistException;
    List<Template> readWithChildren(Integer id) throws PersistException;
}

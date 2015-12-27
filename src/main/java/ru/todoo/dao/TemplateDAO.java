package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.entity.TemplateEntity;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public interface TemplateDAO extends GenericDAO<TemplateEntity, Integer> {
    List<TemplateEntity> readAllRoot() throws PersistException;

    List<TemplateEntity> readRootByCategory(Integer categoryId) throws PersistException;

    List<TemplateEntity> readPopularRoot() throws PersistException;
}

package ru.todoo.dao;

import ru.todoo.domain.entity.TemplateEntity;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public interface TemplateDAO extends GenericDAO<TemplateEntity, Integer> {
    List<TemplateEntity> readAllRoot();

    List<TemplateEntity> readRootByCategory(Integer categoryId);

    List<TemplateEntity> readPopularRoot();
}

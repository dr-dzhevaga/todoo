package ru.todoo.dao.hibernate;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.domain.entity.CategoryEntity;

import javax.persistence.EntityManager;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateCategoryDAO extends HibernateGenericDAO<CategoryEntity, Integer> implements CategoryDAO {
    public HibernateCategoryDAO(EntityManager entityManager) {
        super(CategoryEntity.class, entityManager);
    }
}

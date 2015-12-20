package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import ru.todoo.dao.CategoryDAO;
import ru.todoo.domain.entity.CategoryEntity;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateCategoryDAO extends HibernateGenericDAO<CategoryEntity, Integer> implements CategoryDAO {
    public HibernateCategoryDAO(Session session) {
        super(CategoryEntity.class, session);
    }
}

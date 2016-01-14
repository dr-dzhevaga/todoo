package ru.todoo.dao.hibernate;

import org.springframework.stereotype.Repository;
import ru.todoo.dao.CategoryDAO;
import ru.todoo.domain.entity.CategoryEntity;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Repository
public class HibernateCategoryDAO extends HibernateGenericDAO<CategoryEntity, Integer> implements CategoryDAO {
    public HibernateCategoryDAO() {
        super(CategoryEntity.class);
    }
}
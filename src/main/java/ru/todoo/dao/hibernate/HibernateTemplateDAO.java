package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.entity.TemplateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Repository
public class HibernateTemplateDAO extends HibernateHierarchicalDAO<TemplateEntity, Integer> implements TemplateDAO {
    public HibernateTemplateDAO() {
        super(TemplateEntity.class);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TemplateEntity> readAllRoot() {
        return entityManager.unwrap(Session.class).
                createCriteria(TemplateEntity.class).add(Restrictions.isNull("parent")).list();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TemplateEntity> readRootByCategory(Integer categoryId) {
        return entityManager.unwrap(Session.class).
                createCriteria(TemplateEntity.class).add(Restrictions.isNull("parent")).
                createAlias("category", "category").add(Restrictions.eq("category.id", categoryId)).
                list();
    }

    @Override
    public List<TemplateEntity> readPopularRoot() {
        return new ArrayList<>();
    }
}

package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.entity.TemplateEntity;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateTemplateDAO extends HibernateHierarhicalDAO<TemplateEntity, Integer> implements TemplateDAO {
    public HibernateTemplateDAO(EntityManager entityManager) {
        super(TemplateEntity.class, entityManager);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TemplateEntity> readAllRoot() throws PersistException {
        return entityManager.unwrap(Session.class).
                createCriteria(TemplateEntity.class).add(Restrictions.isNull("parent")).list();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TemplateEntity> readRootByCategory(Integer categoryId) throws PersistException {
        return entityManager.unwrap(Session.class).
                createCriteria(TemplateEntity.class).add(Restrictions.isNull("parent")).
                createAlias("category", "category").add(Restrictions.eq("category.id", categoryId)).
                list();
    }

    @Override
    public List<TemplateEntity> readPopularRoot() throws PersistException {
        return new ArrayList<>();
    }
}

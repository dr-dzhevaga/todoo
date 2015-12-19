package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateTemplateDAO extends HibernateGenericDAO<Template, Integer> implements TemplateDAO {
    public HibernateTemplateDAO(Session session) {
        super(Template.class, session);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Template> readByCategory(Integer categoryId) throws PersistException {
        return session.createCriteria(Template.class).createAlias("category", "category").add(Restrictions.eq("category.name", categoryId)).list();
    }

    @Override
    public List<Template> readPopular() throws PersistException {
        return new ArrayList<>();
    }
}

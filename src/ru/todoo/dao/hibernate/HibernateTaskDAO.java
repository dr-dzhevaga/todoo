package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.entity.TaskEntity;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateTaskDAO extends HibernateGenericDAO<TaskEntity, Integer> implements TaskDAO {
    public HibernateTaskDAO(Session session) {
        super(TaskEntity.class, session);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TaskEntity> readRootByUser(Integer userId) throws PersistException {
        return session.createCriteria(TaskEntity.class).add(Restrictions.isNull("parent")).createAlias("user", "user").add(Restrictions.eq("user.id", userId)).list();
    }
}

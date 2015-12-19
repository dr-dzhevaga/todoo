package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateTaskDAO extends HibernateGenericDAO<Task, Integer> implements TaskDAO {
    public HibernateTaskDAO(Session session) {
        super(Task.class, session);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Task> readByUser(Integer userId) throws PersistException {
        return session.createCriteria(Task.class).createAlias("user", "user").add(Restrictions.eq("user.id", userId)).list();
    }
}

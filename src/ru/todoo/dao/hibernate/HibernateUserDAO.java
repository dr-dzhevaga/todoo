package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.User;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateUserDAO extends HibernateGenericDAO<User, Integer> implements UserDAO {
    public HibernateUserDAO(Session session) {
        super(User.class, session);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<User> readByLogin(String login) throws PersistException {
        return session.createCriteria(User.class).add(Restrictions.eq("login", login)).list();
    }
}

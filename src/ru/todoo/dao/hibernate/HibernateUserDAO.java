package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.entity.UserEntity;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateUserDAO extends HibernateGenericDAO<UserEntity, Integer> implements UserDAO {
    public HibernateUserDAO(Session session) {
        super(UserEntity.class, session);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UserEntity readByLogin(String login) throws PersistException {
        return (UserEntity) session.createCriteria(UserEntity.class).add(Restrictions.eq("login", login)).list().
                stream().findFirst().orElse(null);
    }
}
package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.entity.UserEntity;

import javax.persistence.EntityManager;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateUserDAO extends HibernateGenericDAO<UserEntity, Integer> implements UserDAO {
    public HibernateUserDAO(EntityManager entityManager) {
        super(UserEntity.class, entityManager);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UserEntity readByLogin(String login) throws PersistException {
        return (UserEntity) entityManager.unwrap(Session.class).
                createCriteria(UserEntity.class).add(Restrictions.eq("login", login)).list().
                stream().findFirst().orElse(null);
    }
}
package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.entity.UserEntity;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Repository
public class HibernateUserDAO extends HibernateGenericDAO<UserEntity, Integer> implements UserDAO {
    public HibernateUserDAO() {
        super(UserEntity.class);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UserEntity readByLogin(String login) {
        return (UserEntity) entityManager.unwrap(Session.class).
                createCriteria(UserEntity.class).add(Restrictions.eq("username", login)).list().
                stream().findFirst().orElse(null);
    }
}
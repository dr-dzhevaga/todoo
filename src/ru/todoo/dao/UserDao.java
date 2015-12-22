package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.entity.UserEntity;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface UserDAO extends GenericDAO<UserEntity, Integer> {
    UserEntity readByLogin(String login) throws PersistException;
}

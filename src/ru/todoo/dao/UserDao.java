package ru.todoo.dao;

import ru.todoo.domain.User;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface UserDAO extends GenericDAO<User, Integer> {
    User getByLogin(String login) throws PersistException;
}

package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.User;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface UserDAO extends GenericDAO<User, Integer> {
    List<User> getByLogin(String login) throws PersistException;
}

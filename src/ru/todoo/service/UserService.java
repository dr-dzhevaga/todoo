package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.User;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class UserService {
    private final DerbyDAOHelper<UserDAO> daoHelper;

    public UserService() throws PersistException {
        daoHelper = new DerbyDAOHelper<>(UserDAO.class);
    }

    public void add(User user) throws PersistException {

    }

    public User read(Integer userId) throws PersistException {
        return daoHelper.read(userDAO -> userDAO.read(userId));
    }

    public User readByLogin(String login) throws PersistException {
        return daoHelper.read(userDAO -> userDAO.readByLogin(login)).stream().findFirst().orElse(null);
    }

    public void delete(Integer userId) throws PersistException {
        daoHelper.executeProcedure(userDAO -> userDAO.delete(userId));
    }

    public void changePassword(Integer userId, String newPassword, String oldPassword) {

    }

    public boolean isLoginUnique(String login) throws PersistException {
        List<User> users = daoHelper.read(userDAO -> userDAO.readByLogin(login));
        return users.isEmpty();
    }
}

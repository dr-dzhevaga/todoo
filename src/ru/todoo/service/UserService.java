package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class UserService {
    private final DerbyDAOHelper<UserDAO> daoHelper = new DerbyDAOHelper<>(UserDAO.class);

    public void addUser(User user) throws PersistException, SQLException {
        // TODO - check login and create
    }

    public User getUser(Integer userId) throws PersistException, SQLException {
        return daoHelper.read(userDAO -> userDAO.read(userId));
    }

    public void deleteUser(Integer userId) throws PersistException, SQLException {
        daoHelper.executeProcedure(userDAO -> userDAO.delete(userId));
    }

    public void changePassword(Integer userId, String newPassword, String oldPassword) {
        // TODO - check password and execute
    }

    public boolean isLoginUnique(String login) throws PersistException, SQLException {
        List<User> users = daoHelper.read(userDAO -> userDAO.getByLogin(login));
        return users.isEmpty();
    }
}

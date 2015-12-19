package ru.todoo.service;

import org.apache.commons.lang3.StringUtils;
import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class UserService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "user";
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();

    public void create(User user) throws PersistException {
        if (StringUtils.isBlank(user.getLogin()) || StringUtils.isBlank(user.getPassword())) {
            throw new PersistException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(user.getLogin())) {
            throw new PersistException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_USER_ROLE);
        user.setRoles(roles);
        daoUtil.executeOnDAO(UserDAO.class, userDAO -> userDAO.create(user));
    }

    public User readByLogin(String login) throws PersistException {
        return daoUtil.callOnDAO(UserDAO.class, userDAO ->
                userDAO.readByLogin(login)).stream().findFirst().orElse(null);
    }

    public void delete(Integer userId) throws PersistException {
        daoUtil.executeOnDAO(UserDAO.class, userDAO -> userDAO.delete(userId));
    }

    public boolean isLoginUnique(String login) throws PersistException {
        List<User> users = daoUtil.callOnDAO(UserDAO.class, userDAO -> userDAO.readByLogin(login));
        return users.isEmpty();
    }
}
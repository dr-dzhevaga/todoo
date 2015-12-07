package ru.todoo.service;

import org.apache.commons.lang3.StringUtils;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.RoleDAO;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.Role;
import ru.todoo.domain.User;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class UserService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "user";

    private final DerbyDAOHelper<UserDAO> userDaoHelper;
    private final DerbyDAOHelper<RoleDAO> roleDaoHelper;

    public UserService() throws PersistException {
        userDaoHelper = new DerbyDAOHelper<>(UserDAO.class);
        roleDaoHelper = new DerbyDAOHelper<>(RoleDAO.class);
    }

    public void create(User user) throws PersistException {
        if (StringUtils.isBlank(user.getLogin()) || StringUtils.isBlank(user.getPassword())) {
            throw new PersistException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(user.getLogin())) {
            throw new PersistException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        userDaoHelper.executeProcedure(userDAO -> userDAO.create(user));
        Role role = new Role();
        role.setLogin(user.getLogin());
        role.setRole(DEFAULT_USER_ROLE);
        roleDaoHelper.executeProcedure(roleDAO -> roleDAO.addUserRole(user, role));
    }

    public User readByLogin(String login) throws PersistException {
        return userDaoHelper.read(userDAO -> userDAO.readByLogin(login)).stream().findFirst().orElse(null);
    }

    public void delete(Integer userId) throws PersistException {
        userDaoHelper.executeProcedure(userDAO -> userDAO.delete(userId));
    }

    public boolean isLoginUnique(String login) throws PersistException {
        List<User> users = userDaoHelper.read(userDAO -> userDAO.readByLogin(login));
        return users.isEmpty();
    }
}

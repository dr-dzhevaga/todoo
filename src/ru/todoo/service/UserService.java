package ru.todoo.service;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class UserService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "user";
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    public void create(UserDTO dto) throws PersistException {
        if (StringUtils.isBlank(dto.getLogin()) || StringUtils.isBlank(dto.getPassword())) {
            throw new PersistException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(dto.getLogin())) {
            throw new PersistException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_USER_ROLE);
        UserEntity entity = mapper.map(dto, UserEntity.class);
        entity.setRoles(roles);
        daoUtil.execute(UserDAO.class, dao -> dao.create(entity));
    }

    public UserDTO readByLogin(String login) throws PersistException {
        return daoUtil.call(UserDAO.class, dao -> {
            UserEntity entity = dao.readByLogin(login);
            return mapper.map(entity, UserDTO.class);
        });
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.execute(UserDAO.class, dao -> dao.delete(id));
    }

    public boolean isLoginUnique(String login) throws PersistException {
        UserEntity entity = daoUtil.call(UserDAO.class, dao -> dao.readByLogin(login));
        return entity == null;
    }
}
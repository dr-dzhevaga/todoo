package ru.todoo.service;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.UserEntity;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
public class UserService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "user";

    @Autowired
    UserDAO userDAO;

    @Autowired
    private Mapper mapper;

    @Transactional
    public void create(UserDTO dto) {
        if (StringUtils.isBlank(dto.getLogin()) || StringUtils.isBlank(dto.getPassword())) {
            throw new PersistenceException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(dto.getLogin())) {
            throw new PersistenceException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_USER_ROLE);
        UserEntity entity = mapper.map(dto, UserEntity.class);
        entity.setRoles(roles);
        userDAO.create(entity);
    }

    @Transactional(readOnly = true)
    public UserDTO readByLogin(String login) {
        UserEntity entity = userDAO.readByLogin(login);
        return mapper.map(entity, UserDTO.class);
    }

    @Transactional
    public void delete(Integer id) {
        userDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public boolean isLoginUnique(String login) {
        UserEntity entity = userDAO.readByLogin(login);
        return entity == null;
    }
}
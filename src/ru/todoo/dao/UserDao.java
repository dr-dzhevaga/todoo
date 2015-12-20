package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.entity.UserEntity;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface UserDAO extends GenericDAO<UserEntity, Integer> {
    List<UserEntity> readByLogin(String login) throws PersistException;
}

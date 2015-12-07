package ru.todoo.dao;

import ru.todoo.domain.Role;
import ru.todoo.domain.User;

/**
 * Created by Dmitriy Dzhevaga on 07.12.2015.
 */
public interface RoleDAO {
    void addUserRole(User user, Role role) throws PersistException;
}

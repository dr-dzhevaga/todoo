package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.RoleDAO;
import ru.todoo.dao.generic.JDBCHelper;
import ru.todoo.domain.Role;
import ru.todoo.domain.User;

import java.sql.Connection;

/**
 * Created by dmitriy.dzhevaga on 07.12.2015.
 */
public class DerbyRoleDAO implements RoleDAO {
    private final JDBCHelper jdbcHelper;

    public DerbyRoleDAO(Connection connection) {
        this.jdbcHelper = new JDBCHelper(connection);
    }

    @Override
    public void addUserRole(User user, Role role) throws PersistException {
        String sql = "INSERT INTO roles (login, role) VALUES (?, ?)";
        jdbcHelper.update(sql, new Object[]{user.getLogin(), role.getRole()});
    }
}

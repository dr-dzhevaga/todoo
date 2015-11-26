package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;
import ru.todoo.dao.generic.GenericDAOJDBCImpl;
import ru.todoo.domain.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class DerbyUserDAO extends GenericDAOJDBCImpl<User, Integer> implements UserDAO {

    public DerbyUserDAO(Connection connection) {
        super(connection, "users");
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + table + " (" +
                "login, " +
                "password) " +
                "VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + table + " SET " +
                "login = ?, " +
                "password = ?, " +
                "modified = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
    }

    @Override
    protected User parseResultSet(ResultSet rs) throws SQLException {
        PersistUser user = new PersistUser();
        user.setId(rs.getInt("id"));
        user.setCreated(rs.getTimestamp("created"));
        user.setModified(rs.getTimestamp("modified"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    @Override
    protected Object[] getParametersForInsert(User user) {
        return new Object[]{
                user.getLogin(),
                user.getPassword()
        };
    }

    @Override
    protected Object[] getParametersForUpdate(User user) {
        return new Object[]{
                user.getLogin(),
                user.getPassword(),
                user.getId()
        };
    }

    @Override
    public List<User> getByLogin(String login) throws PersistException {
        String sql = getSelectQuery() + " WHERE login = ?";
        return jdbcHelper.select(sql, new Object[]{login}, this::parseResultSet);
    }

    private static class PersistUser extends User {
        @Override
        public void setId(Integer id) {
            super.setId(id);
        }

        @Override
        public void setCreated(Timestamp created) {
            super.setCreated(created);
        }
    }
}

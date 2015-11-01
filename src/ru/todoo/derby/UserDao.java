package ru.todoo.derby;

import ru.todoo.dao.AbstractJDBCDao;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class UserDao extends AbstractJDBCDao<User, Integer> {

    public UserDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM users";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO users (login, password) VALUES (?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE users SET login = ?, password = ?, modified = CURRENT_TIMESTAMP WHERE id= ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM users WHERE id= ?";
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
    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistException {
        List<User> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistUser user = new PersistUser();
                user.setId(rs.getInt("id"));
                user.setCreated(rs.getTimestamp("created"));
                user.setModified(rs.getTimestamp("modified"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}

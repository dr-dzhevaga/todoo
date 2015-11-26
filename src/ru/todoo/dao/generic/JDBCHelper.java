package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 08.11.2015.
 */
public class JDBCHelper {
    private final Connection connection;

    public JDBCHelper(Connection connection) {
        this.connection = connection;
    }

    private static void setParameters(PreparedStatement statement, Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }

    public int update(String sql, Object[] parameters) throws PersistException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public Object updateAndReturnGeneratedKey(String sql, Object[] parameters) throws PersistException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(statement, parameters);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new PersistException("No id generated on create");
            }
            return generatedKeys.getObject(1);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public <T> List<T> select(String sql,
                              Object[] parameters,
                              ThrowingFunction<ResultSet, T, SQLException> resultSetParser)
            throws PersistException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);
            ResultSet rs = statement.executeQuery();
            List<T> result = new LinkedList<>();
            while (rs.next()) {
                result.add(resultSetParser.apply(rs));
            }
            return result;
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}

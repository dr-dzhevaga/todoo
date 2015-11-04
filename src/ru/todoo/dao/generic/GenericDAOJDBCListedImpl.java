package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 03.11.2015.
 */
public abstract class GenericDAOJDBCListedImpl<T extends Identified<PK> & Listed<PK>, PK extends Serializable> extends GenericDAOJDBCImpl<T, PK> {

    public GenericDAOJDBCListedImpl(Connection connection, String table) {
        super(connection, table);
    }

    public abstract String getCreateQuery();

    public abstract String getUpdateQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    @Override
    public PK create(T newInstance) throws PersistException {
        PK id = super.create(newInstance);
        T persistObject = read(id);
        onChildAdd(persistObject);
        return id;
    }

    @Override
    public void update(T transientObject) throws PersistException {
        T persistObject = read(transientObject.getId());
        super.update(transientObject);
        if (!Objects.equals(transientObject.getParentId(), persistObject.getParentId())) {
            onParentChange(persistObject, transientObject);
        } else if (!Objects.equals(transientObject.getOrder(), persistObject.getOrder())) {
            onOrderChange(persistObject, transientObject);
        }
    }

    @Override
    public void delete(T persistentObject) throws PersistException {
        super.delete(persistentObject);
        onChildRemove(persistentObject);
    }

    private void onChildAdd(T newInstance) throws PersistException {
        String sql = "UPDATE " + table + "\n" +
                "SET order_number = (SELECT CASE WHEN MAX(order_number) IS NULL THEN 0 ELSE (MAX(order_number) + 1) END\n" +
                "                    FROM " + table + "\n" +
                "                    WHERE parent_id = ?)\n" +
                "WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, newInstance.getParentId());
            statement.setObject(2, newInstance.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    private void onChildRemove(T persistentObject) throws PersistException {
        String sql = "UPDATE " + table + "\n" +
                "SET order_number = (order_number - 1)\n" +
                "WHERE parent_id = ? AND order_number > ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, persistentObject.getParentId());
            statement.setInt(2, persistentObject.getOrder());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    private void onParentChange(T persistObject, T transientObject) throws PersistException {
        onChildRemove(persistObject);
        onChildAdd(transientObject);
    }

    private void onOrderChange(T persistObject, T transientObject) throws PersistException {
        if (transientObject.getOrder() > persistObject.getOrder()) {
            // move down
            String sql = "UPDATE " + table + "\n" +
                    "SET order_number = (order_number - 1)\n" +
                    "WHERE parent_id = ? AND id <> ? AND order_number > ? AND order_number <= ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, persistObject.getParentId());
                statement.setObject(2, persistObject.getId());
                statement.setInt(3, persistObject.getOrder());
                statement.setInt(4, transientObject.getOrder());
                statement.execute();
            } catch (SQLException e) {
                throw new PersistException(e, e.getMessage());
            }
        } else {
            // move up
            String sql = "UPDATE " + table + "\n" +
                    "SET order_number = (order_number + 1)\n" +
                    "WHERE parent_id = ? AND id <> ? AND order_number >= ? AND order_number < ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, persistObject.getParentId());
                statement.setObject(2, persistObject.getId());
                statement.setInt(3, transientObject.getOrder());
                statement.setInt(4, persistObject.getOrder());
                statement.execute();
            } catch (SQLException e) {
                throw new PersistException(e, e.getMessage());
            }
        }
    }
}

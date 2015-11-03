package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        String sql = "UPDATE " + table + " SET order_number = (SELECT MAX(order_number) + 1) FROM " + table + " WHERE parent_id = ?) WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, newInstance.getParentId());
            statement.setObject(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
        return id;
    }

    @Override
    public void update(T transientObject) throws PersistException {
        T persistObject = read(transientObject.getId());
        // change parent
        if (transientObject.getParentId() != null &&
                persistObject.getParentId() != null &&
                !transientObject.getParentId().equals(persistObject.getParentId())) {
            String sql = "SELECT MAX(order_number) + 1 FROM " + table + " WHERE parent_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, transientObject.getParentId());
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    Integer order = rs.getInt(1);
                    transientObject.setOrder(order);
                }
            } catch (SQLException e) {
                throw new PersistException(e, e.getMessage());
            }
         // change position
        } else if (transientObject.getOrder() != null &&
                persistObject.getOrder() != null &&
                !transientObject.getOrder().equals(persistObject.getOrder())) {
            // move down
            if (transientObject.getOrder() > persistObject.getOrder()) {
                String sql = "UPDATE " + table + " SET order_number = (order_number - 1) " +
                        "WHERE parent_id = ? AND order_number > ? AND order_number < ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setObject(1, persistObject.getParentId());
                    statement.setInt(2, persistObject.getOrder());
                    statement.setInt(3, transientObject.getOrder());
                    statement.execute();
                } catch (SQLException e) {
                    throw new PersistException(e, e.getMessage());
                }
                // move up
            } else {
                String sql = "UPDATE " + table + " SET order_number = (order_number + 1) " +
                        "WHERE parent_id = ? AND order_number > ? AND order_number < ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setObject(1, persistObject.getParentId());
                    statement.setInt(2, transientObject.getOrder());
                    statement.setInt(3, persistObject.getOrder());
                    statement.execute();
                } catch (SQLException e) {
                    throw new PersistException(e, e.getMessage());
                }
            }
        }
        super.update(transientObject);
    }

    @Override
    public void delete(T persistentObject) throws PersistException {
        super.delete(persistentObject);
        String sql = "UPDATE " + table + " SET order_number = (order_number - 1) " +
                "WHERE parent_id = ? AND order_number > ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, persistentObject.getParentId());
            statement.setObject(2, persistentObject.getOrder());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }
}

package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dmitriy Dzhevga on 04.11.2015.
 */
public abstract class ListedDAOJDBCImpl<T extends Identified<PK> & Listed<PK>, PK extends Serializable> extends GenericDAOJDBCImpl<T, PK> implements ListedDAO<T, PK> {
    public ListedDAOJDBCImpl(Connection connection, String table) {
        super(connection, table);
    }

    @Override
    public List<T> readRoots() throws PersistException {
        List<T> list;
        String sql = getSelectQuery() + " WHERE parent_id IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public List<T> readChildren(T parent) throws PersistException {
        return readChildren(parent, OrderDirection.ASC, SelectionLimit.ALL);
    }

    @Override
    public T readLastChild(T parent) throws PersistException {
        List<T> list = readChildren(parent, OrderDirection.DESC, SelectionLimit.FIRST);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public T readFirstChild(T parent) throws PersistException {
        List<T> list = readChildren(parent, OrderDirection.ASC, SelectionLimit.FIRST);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private List<T> readChildren(T parent, OrderDirection order, SelectionLimit limit) throws PersistException {
        List<T> list;
        String sql = "SELECT * FROM (" + getSelectQuery() + " WHERE parent_id = ?) ordered_table" + order + limit;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parent.getId());
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public List<T> readChildrenRecursive(T parent) throws PersistException {
        List<T> list;
        String sql = "SELECT *\n" +
                "FROM TEMPLATES\n" +
                "WHERE PARENT_ID = ?\n" +
                "UNION\n" +
                "SELECT *\n" +
                "FROM TEMPLATES\n" +
                "WHERE PARENT_ID IN (SELECT ID\n" +
                "                    FROM TEMPLATES\n" +
                "                    WHERE PARENT_ID = ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parent.getId());
            statement.setObject(2, parent.getId());
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public void moveChildrenUp(T parent, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parent, firstChildOrder, lastChildOrder, MoveDirection.DOWN);
    }

    @Override
    public void moveChildrenDown(T parent, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parent, firstChildOrder, lastChildOrder, MoveDirection.UP);
    }

    private void moveChildren(T parent, Integer firstChildOrder, Integer lastChildOrder, MoveDirection direction) throws PersistException {
        String sql = "UPDATE " + table + "\n" +
                "SET order_number = " + direction + "\n" +
                "WHERE parent_id = ? AND order_number >= ? AND order_number <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parent.getId());
            statement.setObject(2, firstChildOrder);
            statement.setInt(3, lastChildOrder);
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    private enum OrderDirection {
        ASC(" ORDER BY order_number ASC"),
        DESC(" ORDER BY order_number DESC");

        private final String queryClause;

        OrderDirection(String queryClause) {
            this.queryClause = queryClause;
        }

        @Override
        public String toString() {
            return queryClause;
        }
    }

    private enum SelectionLimit {
        FIRST(" FETCH FIRST ROW ONLY"),
        ALL("");

        private final String queryClause;

        SelectionLimit(String queryClause) {
            this.queryClause = queryClause;
        }

        @Override
        public String toString() {
            return queryClause;
        }
    }

    private enum MoveDirection {
        UP(" order_number - 1"),
        DOWN(" order_number + 1");

        private final String updatedOrder;

        MoveDirection(String updatedOrder) {
            this.updatedOrder = updatedOrder;
        }

        @Override
        public String toString() {
            return updatedOrder;
        }
    }
}

package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * Created by Dmitriy Dzhevga on 04.11.2015.
 */
public abstract class ListedDAOJDBCImpl<T extends Identified<PK> & Listed<PK>, PK extends Serializable> extends GenericDAOJDBCImpl<T, PK> implements ListedDAO<T, PK> {
    public ListedDAOJDBCImpl(Connection connection, String table) {
        super(connection, table);
    }

    @Override
    public List<T> readChildren(PK parentId) throws PersistException {
        return readChildren(parentId, OrderDirection.ASC, SelectionLimit.ALL);
    }

    @Override
    public T readLastChild(PK parentId) throws PersistException {
        List<T> list = readChildren(parentId, OrderDirection.DESC, SelectionLimit.FIRST);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public T readFirstChild(PK parentId) throws PersistException {
        List<T> list = readChildren(parentId, OrderDirection.ASC, SelectionLimit.FIRST);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private List<T> readChildren(PK parentId, OrderDirection order, SelectionLimit limit) throws PersistException {
        String sql = "SELECT * FROM (" + getSelectQuery() + " WHERE parent_id = ?) ordered_table" + order + limit;
        return jdbcHelper.select(sql, new Object[]{parentId}, this::parseResultSet);
    }

    @Override
    public List<T> readChildrenRecursive(PK parentId) throws PersistException {
        String sql = "SELECT *\n" +
                "FROM " + table + "\n" +
                "WHERE PARENT_ID = ?\n" +
                "UNION\n" +
                "SELECT *\n" +
                "FROM " + table + "\n" +
                "WHERE PARENT_ID IN (SELECT ID\n" +
                "                    FROM " + table + "\n" +
                "                    WHERE PARENT_ID = ?)";
        return jdbcHelper.select(sql, new Object[]{parentId, parentId}, this::parseResultSet);
    }

    @Override
    public void moveChildrenUp(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parentId, firstChildOrder, lastChildOrder, MoveDirection.DOWN);
    }

    @Override
    public void moveChildrenDown(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parentId, firstChildOrder, lastChildOrder, MoveDirection.UP);
    }

    private void moveChildren(PK parentId, Integer firstChildOrder, Integer lastChildOrder, MoveDirection direction) throws PersistException {
        String sql = "UPDATE " + table + "\n" +
                "SET order_number = " + direction + "\n" +
                "WHERE parent_id = ? AND order_number >= ? AND order_number <= ?";
        jdbcHelper.update(sql, new Object[]{parentId, firstChildOrder, lastChildOrder});
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

        private final String orderValue;

        MoveDirection(String orderValue) {
            this.orderValue = orderValue;
        }

        @Override
        public String toString() {
            return orderValue;
        }
    }
}

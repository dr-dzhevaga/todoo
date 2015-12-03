package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevga on 04.11.2015.
 */
public abstract class ListedDAOJDBCImpl<T extends Identified<PK> & Listed<PK>, PK extends Serializable> extends GenericDAOJDBCImpl<T, PK> implements ListedDAO<T, PK> {
    public ListedDAOJDBCImpl(Connection connection, String table) {
        super(connection, table);
    }

    @Override
    public List<T> readHierarchy(PK parentId) throws PersistException {
        String sql = "SELECT *\n" +
                "FROM " + table + "\n" +
                "WHERE ID = ? OR PARENT_ID = ?\n" +
                "UNION\n" +
                "SELECT *\n" +
                "FROM " + table + "\n" +
                "WHERE PARENT_ID IN (SELECT ID\n" +
                "                    FROM " + table + "\n" +
                "                    WHERE PARENT_ID = ?)\n" +
                "ORDER BY ORDER_NUMBER";
        return jdbcHelper.select(sql, new Object[]{parentId, parentId, parentId}, this::parseResultSet);
    }

    @Override
    public T create(T newInstance) throws PersistException {
        if (newInstance.getParentId() != null) {
            // limit hierarchy level by 2
            T parent = read(newInstance.getParentId());
            if (parent.getParentId() != null) {
                T grandParent = read(parent.getParentId());
                if (grandParent.getParentId() != null) {
                    newInstance.setParentId(parent.getParentId());
                }
            }
            // set new task to the end of the list of children
            T lastNeighbor = readLastChild(newInstance.getParentId());
            if (lastNeighbor != null) {
                newInstance.setOrder(lastNeighbor.getOrder() + 1);
            } else {
                newInstance.setOrder(0);
            }
        }
        return super.create(newInstance);
    }

    @Override
    public void delete(PK id) throws PersistException {
        T deletedInstance = read(id);
        // move neighbors
        if (deletedInstance.getParentId() != null) {
            T lastNeighbor = readLastChild(deletedInstance.getParentId());
            if (lastNeighbor != null && !Objects.equals(lastNeighbor.getId(), deletedInstance.getId())) {
                moveChildrenUp(deletedInstance.getParentId(), deletedInstance.getOrder() + 1, lastNeighbor.getOrder());
            }
        }
        deleteHierarchy(id);
    }

    private void deleteHierarchy(PK id) throws PersistException {
        String sql = "DELETE FROM " + table + "\n" +
                "WHERE id IN (SELECT id\n" +
                "             FROM " + table + "\n" +
                "             WHERE ID = ? OR PARENT_ID = ?\n" +
                "             UNION\n" +
                "             SELECT id\n" +
                "             FROM " + table + "\n" +
                "             WHERE PARENT_ID IN (SELECT ID\n" +
                "                                 FROM " + table + "\n" +
                "                                 WHERE PARENT_ID = ?))";
        jdbcHelper.update(sql, new Object[]{id, id, id});
    }

    @Override
    public void update(T updatedInstance) throws PersistException {
        T originInstance = read(updatedInstance.getId());
        // update parent
        if (!Objects.equals(originInstance.getParentId(), updatedInstance.getParentId())) {
            updateParent(updatedInstance.getId(), updatedInstance.getParentId());
            // or update order
        } else if (!Objects.equals(originInstance.getOrder(), updatedInstance.getOrder())) {
            updateOrder(updatedInstance.getId(), updatedInstance.getOrder());
            // or update parameters
        } else {
            super.update(updatedInstance);
        }
    }

    private void updateOrder(PK id, Integer newOrder) throws PersistException {
        T originInstance = read(id);
        if (newOrder > originInstance.getOrder()) {
            moveChildrenUp(originInstance.getParentId(), originInstance.getOrder() + 1, newOrder);
        } else if (newOrder < originInstance.getOrder()) {
            moveChildrenDown(originInstance.getParentId(), newOrder, originInstance.getOrder() - 1);
        }
        originInstance.setOrder(newOrder);
        super.update(originInstance);
    }

    private void updateParent(PK id, PK newParentId) throws PersistException {
        T originInstance = read(id);
        if (originInstance.getParentId() != null) {
            T oldLastNeighbor = readLastChild(originInstance.getParentId());
            if (oldLastNeighbor != null) {
                moveChildrenUp(originInstance.getParentId(), originInstance.getOrder() + 1, oldLastNeighbor.getOrder());
            }
        }
        T lastNeighbor = readLastChild(newParentId);
        if (lastNeighbor != null) {
            originInstance.setOrder(lastNeighbor.getOrder() + 1);
        } else {
            originInstance.setOrder(0);
        }
        originInstance.setParentId(newParentId);
        super.update(originInstance);
    }

    private T readFirstChild(PK parentId) throws PersistException {
        List<T> list = readChildren(parentId, OrderDirection.ASC, SelectionLimit.FIRST);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private T readLastChild(PK parentId) throws PersistException {
        List<T> list = readChildren(parentId, OrderDirection.DESC, SelectionLimit.FIRST);
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

    private void moveChildrenUp(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parentId, firstChildOrder, lastChildOrder, MoveDirection.UP);
    }

    private void moveChildrenDown(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException {
        moveChildren(parentId, firstChildOrder, lastChildOrder, MoveDirection.DOWN);
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

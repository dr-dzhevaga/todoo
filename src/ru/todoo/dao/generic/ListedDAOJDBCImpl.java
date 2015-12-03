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
            // set the new child to the end of the new parent list
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
        // move up children after the deleted child
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
            updateParent(originInstance, updatedInstance.getParentId());
            // or update order
        } else if (!Objects.equals(originInstance.getOrder(), updatedInstance.getOrder())) {
            updateOrder(originInstance, updatedInstance.getOrder());
            // or update parameters
        } else {
            super.update(updatedInstance);
        }
    }

    private void updateOrder(T originInstance, Integer newOrder) throws PersistException {
        Integer oldOrder = originInstance.getOrder();
        if (newOrder > oldOrder) {
            moveChildrenUp(originInstance.getParentId(), oldOrder + 1, newOrder);
        } else if (newOrder < oldOrder) {
            moveChildrenDown(originInstance.getParentId(), newOrder, oldOrder - 1);
        }
        originInstance.setOrder(newOrder);
        super.update(originInstance);
    }

    private void updateParent(T originInstance, PK newParentId) throws PersistException {
        // move up children after the moved child
        if (originInstance.getParentId() != null) {
            T oldLastNeighbor = readLastChild(originInstance.getParentId());
            if (oldLastNeighbor != null) {
                moveChildrenUp(originInstance.getParentId(), originInstance.getOrder() + 1, oldLastNeighbor.getOrder());
            }
        }
        // set the moved child to the end of the new parent list
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
        return readChild(parentId, OrderDirection.ASC);
    }

    private T readLastChild(PK parentId) throws PersistException {
        return readChild(parentId, OrderDirection.DESC);
    }

    private T readChild(PK parentId, OrderDirection order) throws PersistException {
        return readChildren(parentId, order, SelectionLimit.FIRST).stream().findFirst().orElse(null);
    }

    private List<T> readChildren(PK parentId, OrderDirection order, SelectionLimit limit) throws PersistException {
        String sql = "SELECT * FROM (" + getSelectQuery() + " WHERE parent_id = ?) ordered_table" + order + limit;
        return jdbcHelper.select(sql, new Object[]{parentId}, this::parseResultSet);
    }

    private void moveChildrenUp(PK parentId, Integer first, Integer last) throws PersistException {
        moveChildren(parentId, first, last, MoveDirection.UP);
    }

    private void moveChildrenDown(PK parentId, Integer first, Integer last) throws PersistException {
        moveChildren(parentId, first, last, MoveDirection.DOWN);
    }

    private void moveChildren(PK parentId, Integer first, Integer last, MoveDirection direction) throws PersistException {
        String sql = "UPDATE " + table + "\n" +
                "SET order_number = " + direction + "\n" +
                "WHERE parent_id = ? AND order_number >= ? AND order_number <= ?";
        jdbcHelper.update(sql, new Object[]{parentId, first, last});
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

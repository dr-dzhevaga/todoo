package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.dao.generic.ListedDAOJDBCImpl;
import ru.todoo.domain.Task;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyTaskDAO extends ListedDAOJDBCImpl<Task, Integer> implements TaskDAO {
    public DerbyTaskDAO(Connection connection, String table) {
        super(connection, table);
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO " + table + " (" +
                "parent_id, " +
                "order_number, " +
                "name, " +
                "description, " +
                "is_template, " +
                "category_id, " +
                "is_completed, " +
                "user_id, " +
                "origin_id" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE " + table + " SET " +
                "parent_id = ?, " +
                "order_number = ?, " +
                "name = ?, " +
                "description = ?, " +
                "is_template = ?, " +
                "category_id = ?, " +
                "is_completed = ?, " +
                "user_id = ?, " +
                "origin_id = ?, " +
                "modified = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
    }

    @Override
    protected List<Task> parseResultSet(ResultSet rs) throws PersistException {
        List<Task> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistTask task = new PersistTask();
                task.setId(rs.getInt("id"));
                task.setParentId(rs.getObject("parent_id", Integer.class));
                task.setOrder(rs.getObject("order_number", Integer.class));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                task.setTemplate(rs.getBoolean("is_template"));
                task.setCategoryId(rs.getObject("category_id", Integer.class));
                task.setCompleted(rs.getBoolean("is_completed"));
                task.setUserId(rs.getObject("user_id", Integer.class));
                task.setOriginId(rs.getObject("origin_id", Integer.class));
                task.setCreated(rs.getTimestamp("created"));
                task.setModified(rs.getTimestamp("modified"));
                result.add(task);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Task task) throws PersistException {
        try {
            statement.setObject(1, task.getParentId());
            statement.setObject(2, task.getOrder());
            statement.setString(3, task.getName());
            statement.setString(4, task.getDescription());
            statement.setBoolean(5, task.isTemplate());
            statement.setObject(6, task.getCategoryId());
            statement.setObject(7, task.isCompleted());
            statement.setObject(8, task.getUserId());
            statement.setObject(9, task.getOriginId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Task task) throws PersistException {
        try {
            prepareStatementForInsert(statement, task);
            statement.setInt(10, task.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public List<Task> readTaskTemplatesByCategory(Integer categoryId) throws PersistException {
        List<Task> list;
        String sql = getSelectQuery() + " WHERE is_template = TRUE AND parent_id IS NULL AND category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
        return list;
    }

    @Override
    public List<Task> readPopularTaskTemplates() throws PersistException {
        List<Task> list;
        String sql = "SELECT *\n" +
                "FROM TASKS\n" +
                "WHERE ID IN (SELECT id\n" +
                "             FROM (\n" +
                "                    SELECT\n" +
                "                      template.id,\n" +
                "                      COUNT(*) count\n" +
                "                    FROM TASKS template JOIN TASKS task ON template.id = task.origin_id\n" +
                "                    GROUP BY template.id\n" +
                "                  ) statistic\n" +
                "             ORDER BY count DESC FETCH FIRST 10 ROWS ONLY)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
        return list;
    }

    @Override
    public List<Task> readTasksByUser(Integer userId) throws PersistException {
        List<Task> list;
        String sql = getSelectQuery() + " WHERE is_template = FALSE AND parent_id IS NULL AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
        return list;
    }

    private static class PersistTask extends Task {
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

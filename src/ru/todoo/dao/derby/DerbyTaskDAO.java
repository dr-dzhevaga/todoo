package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.dao.generic.ListedDAOJDBCImpl;
import ru.todoo.domain.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyTaskDAO extends ListedDAOJDBCImpl<Task, Integer> implements TaskDAO {
    public DerbyTaskDAO(Connection connection) {
        super(connection, "task");
    }

    @Override
    protected String getCreateQuery() {
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
    protected String getUpdateQuery() {
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
    protected Task parseResultSet(ResultSet rs) throws SQLException {
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
        return task;
    }

    @Override
    protected Object[] getParametersForInsert(Task task) {
        return new Object[]{
                task.getParentId(),
                task.getOrder(),
                task.getName(),
                task.getDescription(),
                task.isTemplate(),
                task.getCategoryId(),
                task.isCompleted(),
                task.getUserId(),
                task.getOriginId()
        };
    }

    @Override
    protected Object[] getParametersForUpdate(Task task) {
        return new Object[]{
                task.getParentId(),
                task.getOrder(),
                task.getName(),
                task.getDescription(),
                task.isTemplate(),
                task.getCategoryId(),
                task.isCompleted(),
                task.getUserId(),
                task.getOriginId(),
                task.getId()
        };
    }

    @Override
    public List<Task> readTaskTemplatesByCategory(Integer categoryId) throws PersistException {
        String sql = getSelectQuery() + " WHERE is_template = TRUE AND parent_id IS NULL AND category_id = ?";
        return jdbcHelper.select(sql, new Object[]{categoryId}, this::parseResultSet);
    }

    @Override
    public List<Task> readPopularTaskTemplates() throws PersistException {
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
        return jdbcHelper.select(sql, new Object[0], this::parseResultSet);
    }

    @Override
    public List<Task> readTasksByUser(Integer userId) throws PersistException {
        String sql = getSelectQuery() + " WHERE is_template = FALSE AND parent_id IS NULL AND user_id = ?";
        return jdbcHelper.select(sql, new Object[]{userId}, this::parseResultSet);
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

package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.dao.generic.GenericDAOJDBCImpl;
import ru.todoo.domain.Template;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyTemplateDAO extends GenericDAOJDBCImpl<Template, Integer> implements TemplateDAO {
    public DerbyTemplateDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM templates";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO templates (parent_id, name, description, category_id, order_number)\n" +
                "VALUES (?, ?, ?, ?, (SELECT MAX(order_number) + 1\n" +
                "                     FROM templates\n" +
                "                     WHERE parent_id = ?))";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE templates SET parent_id = ?, name = ?, description = ?, category_id = ? WHERE id= ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM templates WHERE id = ?";
    }

    @Override
    protected List<Template> parseResultSet(ResultSet rs) throws PersistException {
        List<Template> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistTemplate template = new PersistTemplate();
                template.setId(rs.getInt("id"));
                template.setParentId(rs.getInt("parent_id"));
                template.setName(rs.getString("name"));
                template.setDescription(rs.getString("description"));
                template.setOrder(rs.getInt("order_number"));
                template.setCategoryId(rs.getInt("category_id"));
                template.setCreated(rs.getTimestamp("created"));
                result.add(template);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Template template) throws PersistException {
        try {
            statement.setInt(1, template.getParentId());
            statement.setString(2, template.getName());
            statement.setString(3, template.getDescription());
            statement.setInt(4, template.getCategoryId());
            statement.setInt(5, template.getParentId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Template template) throws PersistException {
        try {
            statement.setInt(1, template.getParentId());
            statement.setString(2, template.getName());
            statement.setString(3, template.getDescription());
            statement.setInt(4, template.getCategoryId());
            statement.setInt(5, template.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public List<Template> getByCategoryId(Integer categoryId) throws PersistException {
        List<Template> list;
        String sql = getSelectQuery() + " WHERE category_id = ?";
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
    public void delete(Template persistentObject) throws PersistException {
        super.delete(persistentObject);
        String sql = "UPDATE templates SET order_number = (order_number - 1) " +
                "WHERE parent_id = ? AND order_number > ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, persistentObject.getParentId());
            statement.setInt(2, persistentObject.getOrder());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    @Override
    public void update(Template transientTemplate) throws PersistException {
        Template persistTemplate = read(transientTemplate.getId());
        super.update(transientTemplate);
        // change parent
        if (transientTemplate.getParentId() != null &&
                persistTemplate.getParentId() != null &&
                !transientTemplate.getParentId().equals(persistTemplate.getParentId())) {
            String sql = "SELECT MAX(order_number) + 1 FROM templates WHERE parent_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, transientTemplate.getParentId());
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    Integer order = rs.getInt(1);
                    transientTemplate.setOrder(order);
                }
            } catch (SQLException e) {
                throw new PersistException(e, e.getMessage());
            }
        // change position
        } else if (transientTemplate.getOrder() != null &&
                persistTemplate.getOrder() != null &&
                !transientTemplate.getOrder().equals(persistTemplate.getOrder())) {
            // move down
            if (transientTemplate.getOrder() > persistTemplate.getOrder()) {
                String sql = "UPDATE templates SET order_number = (order_number - 1) " +
                        "WHERE parent_id = ? AND order_number > ? AND order_number < ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, persistTemplate.getParentId());
                    statement.setInt(2, persistTemplate.getOrder());
                    statement.setInt(3, transientTemplate.getOrder());
                    statement.execute();
                } catch (SQLException e) {
                    throw new PersistException(e, e.getMessage());
                }
            // move up
            } else {
                String sql = "UPDATE templates SET order_number = (order_number + 1) " +
                        "WHERE parent_id = ? AND order_number > ? AND order_number < ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, persistTemplate.getParentId());
                    statement.setInt(2, transientTemplate.getOrder());
                    statement.setInt(3, persistTemplate.getOrder());
                    statement.execute();
                } catch (SQLException e) {
                    throw new PersistException(e, e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Template> getPopular() throws PersistException {
        List<Template> list;
        // TODO
        list = new ArrayList<>();
        return list;
    }

    private static class PersistTemplate extends Template {
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

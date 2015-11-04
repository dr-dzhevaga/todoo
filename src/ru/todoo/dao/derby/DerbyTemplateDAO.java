package ru.todoo.dao.derby;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.dao.generic.GenericDAOJDBCListedImpl;
import ru.todoo.domain.Template;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyTemplateDAO extends GenericDAOJDBCListedImpl<Template, Integer> implements TemplateDAO {
    public DerbyTemplateDAO(Connection connection, String table) {
        super(connection, table);
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO " + table + " (parent_id, name, description, category_id) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE " + table + " SET parent_id = ?, name = ?, description = ?, category_id = ?, order_number = ? " +
                "WHERE id= ?";
    }

    @Override
    protected List<Template> parseResultSet(ResultSet rs) throws PersistException {
        List<Template> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistTemplate template = new PersistTemplate();
                template.setId(rs.getInt("id"));
                template.setParentId(rs.getObject("parent_id", Integer.class));
                template.setName(rs.getString("name"));
                template.setDescription(rs.getString("description"));
                template.setOrder(rs.getObject("order_number", Integer.class));
                template.setCategoryId(rs.getObject("category_id", Integer.class));
                template.setCreated(rs.getTimestamp("created"));
                template.setModified(rs.getTimestamp("modified"));
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
            statement.setObject(1, template.getParentId());
            statement.setString(2, template.getName());
            statement.setString(3, template.getDescription());
            statement.setObject(4, template.getCategoryId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Template template) throws PersistException {
        try {
            prepareStatementForInsert(statement, template);
            statement.setInt(5, template.getOrder());
            statement.setInt(6, template.getId());
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

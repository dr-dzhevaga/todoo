package ru.todoo.dao.derby;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAOJDBCImpl;
import ru.todoo.domain.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyCategoryDAO extends GenericDAOJDBCImpl<Category, Integer> implements CategoryDAO {
    public DerbyCategoryDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM categories";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO categories (name) VALUES (?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE categories SET name = ? WHERE id = ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM categories WHERE id = ?";
    }

    @Override
    protected List<Category> parseResultSet(ResultSet rs) throws PersistException {
        List<Category> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistCategory category = new PersistCategory();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                result.add(category);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Category category) throws PersistException {
        try {
            statement.setString(1, category.getName());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Category category) throws PersistException {
        try {
            prepareStatementForInsert(statement, category);
            statement.setInt(2, category.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    private static class PersistCategory extends Category {
        @Override
        public void setId(Integer id) {
            super.setId(id);
        }
    }
}

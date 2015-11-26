package ru.todoo.dao.derby;

import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.generic.GenericDAOJDBCImpl;
import ru.todoo.domain.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public class DerbyCategoryDAO extends GenericDAOJDBCImpl<Category, Integer> implements CategoryDAO {
    public DerbyCategoryDAO(Connection connection) {
        super(connection, "categories");
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO " + table + " (name) VALUES (?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + table + " SET name = ? WHERE id = ?";
    }

    @Override
    protected Category parseResultSet(ResultSet rs) throws SQLException {
        PersistCategory category = new PersistCategory();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    }

    @Override
    protected Object[] getParametersForInsert(Category category) {
        return new Object[]{category.getName()};
    }

    @Override
    protected Object[] getParametersForUpdate(Category category) {
        return new Object[]{category.getName(), category.getId()};
    }

    private static class PersistCategory extends Category {
        @Override
        public void setId(Integer id) {
            super.setId(id);
        }
    }
}

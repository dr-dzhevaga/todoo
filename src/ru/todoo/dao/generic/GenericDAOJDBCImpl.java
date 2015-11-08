package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDAOJDBCImpl<T extends Identified<PK>, PK extends Serializable> implements GenericDAO<T, PK> {
    protected final JDBCHelper jdbcHelper;
    protected Connection connection;
    protected String table;

    public GenericDAOJDBCImpl(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
        this.jdbcHelper = new JDBCHelper(connection);
    }

    protected abstract String getCreateQuery();

    protected abstract String getUpdateQuery();

    protected abstract T parseResultSet(ResultSet rs) throws SQLException;

    protected abstract Object[] getParametersForInsert(T object);

    protected abstract Object[] getParametersForUpdate(T object);

    public String getSelectQuery() {
        return "SELECT * FROM " + table;
    }

    public String getDeleteQuery() {
        return "DELETE FROM " + table + " WHERE id = ?";
    }

    @Override
    public T create(T newInstance) throws PersistException {
        String sql = getCreateQuery();
        Object id = jdbcHelper.executeUpdateAndReturnGeneratedKey(sql, getParametersForInsert(newInstance));
        sql = getSelectQuery() + " WHERE id = ?";
        List<T> list = jdbcHelper.select(sql, new Object[]{id}, this::parseResultSet);
        if (list.isEmpty()) {
            throw new PersistException("Inserted record not found on create");
        }
        if (list.size() > 1) {
            throw new PersistException("More then one record found for generated id on create: " + id);
        }
        return list.get(0);
    }

    @Override
    public T read(PK id) throws PersistException {
        String sql = getSelectQuery() + " WHERE id = ?";
        List<T> list = jdbcHelper.select(sql, new Object[]{id}, this::parseResultSet);
        if (list.isEmpty()) {
            throw new PersistException("Record with id " + id + " not found on get");
        }
        if (list.size() > 1) {
            throw new PersistException("More then one record with id " + id + " found on get");
        }
        return list.get(0);
    }

    @Override
    public void update(T transientObject) throws PersistException {
        String sql = getUpdateQuery();
        int count = jdbcHelper.update(sql, getParametersForUpdate(transientObject));
        if (count != 1) {
            throw new PersistException("More then one record modified on update: " + count);
        }
    }

    @Override
    public void delete(PK id) throws PersistException {
        String sql = getDeleteQuery();
        int count = jdbcHelper.update(sql, new Object[]{id});
        if (count != 1) {
            throw new PersistException("More then one record modified on delete: " + count);
        }
    }

    @Override
    public List<T> readAll() throws PersistException {
        return jdbcHelper.select(getSelectQuery(), new Object[0], this::parseResultSet);
    }
}

package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.sql.*;
import java.util.List;

public abstract class GenericDAOJDBCImpl<T extends Identified<PK>, PK extends Serializable> implements GenericDAO<T, PK> {
    protected Connection connection;
    protected String table;

    public GenericDAOJDBCImpl(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public abstract String getCreateQuery();

    public abstract String getUpdateQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    public String getSelectQuery() {
        return "SELECT * FROM " + table;
    }

    public String getDeleteQuery() {
        return "DELETE FROM " + table + " WHERE id = ?";
    }

    @Override
    public T create(T newInstance) throws PersistException {
        Object id;
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, newInstance);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("More then one record modified on create: " + count);
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new PersistException("No id generated on create");
            }
            id = generatedKeys.getObject(1); // Return BigInteger for INT. So we have to read created record from db to get id with correct type.
        } catch (Exception e) {
            throw new PersistException(e);
        }
        List<T> list;
        sql = getSelectQuery() + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            if (list.isEmpty()) {
                throw new PersistException("Inserted record not found on create");
            }
            if (list.size() > 1) {
                throw new PersistException("More then one record found for generated id on create: " + id);
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return list.get(0);
    }

    @Override
    public T read(PK id) throws PersistException {
        List<T> list;
        String sql = getSelectQuery() + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, transientObject);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("More then one record modified on update: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(T persistentObject) throws PersistException {
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, persistentObject.getId());
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("More then one record modified on delete: " + count);
            }
            statement.close();
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public List<T> readAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }
}

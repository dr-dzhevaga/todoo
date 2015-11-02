package ru.todoo.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public abstract class AbstractJDBCDAO<T extends Identified<PK>, PK extends Serializable> implements GenericDAO<T, PK> {
    protected Connection connection;

    public AbstractJDBCDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract String getSelectQuery();

    public abstract String getCreateQuery();

    public abstract String getUpdateQuery();

    public abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    @Override
    public T create(T object) throws PersistException {
        T newInstance;
        Object id;
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("More then one record modified on create: " + count);
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new PersistException("No id generated on create");
            }
            id = generatedKeys.getObject(1);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        sql = getSelectQuery() + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if (list == null || (list.size() == 0)) {
                throw new PersistException("Inserted record not found on create");
            }
            if (list.size() > 1) {
                throw new PersistException("More then one record found for generated id on create: " + id);
            }
            newInstance = list.iterator().next();
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return newInstance;
    }

    @Override
    public T get(PK id) throws PersistException {
        List<T> list;
        String sql = getSelectQuery() + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistException("Record with id " + id + " not found on get");
        }
        if (list.size() > 1) {
            throw new PersistException("More then one record with id " + id + " found on get");
        }
        return list.get(0);
    }

    @Override
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("More then one record modified on update: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistException {
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getId());
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
    public List<T> getAll() throws PersistException {
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

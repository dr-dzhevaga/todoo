package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.derby.DerbyDAOFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingConsumer;
import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 08.11.2015.
 */
public class DerbyDAOHelper<T> {
    private final DerbyDAOFactory daoFactory;
    private final Class<T> daoClass;

    public DerbyDAOHelper(Class<T> daoClass) throws PersistException {
        this.daoClass = daoClass;
        daoFactory = new DerbyDAOFactory();
    }

    public <R> R read(ThrowingFunction<T, R, PersistException> function) throws PersistException {
        try (Connection connection = daoFactory.getContext()) {
            T dao = daoFactory.getDao(connection, daoClass);
            return function.apply(dao);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public void executeProcedure(ThrowingConsumer<T, PersistException> consumer) throws PersistException {
        try (Connection connection = daoFactory.getContext()) {
            T dao = daoFactory.getDao(connection, daoClass);
            consumer.accept(dao);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public <R> R executeFunction(ThrowingFunction<T, R, PersistException> function) throws PersistException {
        try (Connection connection = daoFactory.getContext()) {
            T dao = daoFactory.getDao(connection, daoClass);
            return function.apply(dao);
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }
}

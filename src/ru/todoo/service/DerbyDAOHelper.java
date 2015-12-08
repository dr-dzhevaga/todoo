package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.derby.JDBCDAOFactory;

import java.sql.Connection;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingConsumer;
import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 08.11.2015.
 */
public class DerbyDAOHelper<T> {
    private final JDBCDAOFactory daoFactory;
    private final Class<T> daoClass;

    public DerbyDAOHelper(Class<T> daoClass) throws PersistException {
        this.daoClass = daoClass;
        daoFactory = JDBCDAOFactory.getInstance();
    }

    public static void executeOnConnection(ThrowingConsumer<Connection, PersistException> consumer, boolean transactional) throws PersistException {
        try (Connection connection = JDBCDAOFactory.getInstance().getContext()) {
            try {
                if (transactional) {
                    connection.setAutoCommit(false);
                }
                consumer.accept(connection);
                if (transactional) {
                    connection.commit();
                }
            } catch (Exception e) {
                if (transactional) {
                    connection.rollback();
                }
                throw e;
            } finally {
                if (transactional) {
                    connection.setAutoCommit(true);
                }
            }
        } catch (Exception e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public static <R> R callOnConnection(ThrowingFunction<Connection, R, PersistException> function, boolean transactional) throws PersistException {
        try (Connection connection = JDBCDAOFactory.getInstance().getContext()) {
            try {
                if (transactional) {
                    connection.setAutoCommit(false);
                }
                R r = function.apply(connection);
                if (transactional) {
                    connection.commit();
                }
                return r;
            } catch (Exception e) {
                if (transactional) {
                    connection.rollback();
                }
                throw e;
            } finally {
                if (transactional) {
                    connection.setAutoCommit(true);
                }
            }
        } catch (Exception e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public void executeOnDAO(ThrowingConsumer<T, PersistException> consumer, boolean transactional) throws PersistException {
        executeOnConnection(connection -> consumer.accept(daoFactory.getDao(connection, daoClass)), transactional);
    }

    public void executeOnDAO(ThrowingConsumer<T, PersistException> consumer) throws PersistException {
        executeOnDAO(consumer, false);
    }

    public <R> R callOnDAO(ThrowingFunction<T, R, PersistException> function, boolean transactional) throws PersistException {
        return callOnConnection(connection -> function.apply(daoFactory.getDao(connection, daoClass)), transactional);
    }

    public <R> R callOnDAO(ThrowingFunction<T, R, PersistException> function) throws PersistException {
        return callOnDAO(function, false);
    }
}

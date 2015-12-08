package ru.todoo.service;

import ru.todoo.dao.DAOFactoryProvider;
import ru.todoo.dao.PersistException;

import java.sql.Connection;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingConsumer;
import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 08.11.2015.
 */
public class DAOHelper {
    private DAOHelper() {
    }

    public static void executeOnContext(boolean transactional, ThrowingConsumer<Object, PersistException> consumer) throws PersistException {
        try (Connection connection = (Connection) DAOFactoryProvider.getDAOFactory().getContext()) {
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

    public static <R> R callOnContext(boolean transactional, ThrowingFunction<Object, R, PersistException> function) throws PersistException {
        try (Connection connection = (Connection) DAOFactoryProvider.getDAOFactory().getContext()) {
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

    public static <T> void executeOnDAO(Class<T> daoClass, boolean transactional, ThrowingConsumer<T, PersistException> consumer) throws PersistException {
        executeOnContext(transactional, context ->
                consumer.accept(DAOFactoryProvider.getDAOFactory().getDao(context, daoClass)));
    }

    public static <R, T> R callOnDAO(Class<T> daoClass, boolean transactional, ThrowingFunction<T, R, PersistException> function) throws PersistException {
        return callOnContext(transactional, context ->
                function.apply(DAOFactoryProvider.getDAOFactory().getDao(context, daoClass)));
    }
}

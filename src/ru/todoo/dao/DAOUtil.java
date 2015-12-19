package ru.todoo.dao;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingConsumer;
import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public interface DAOUtil {
    void executeOnContext(ThrowingConsumer<Object, PersistException> consumer) throws PersistException;

    <R> R callOnContext(ThrowingFunction<Object, R, PersistException> function) throws PersistException;

    default <T> void executeOnDAO(Class<T> daoClass, ThrowingConsumer<T, PersistException> consumer) throws PersistException {
        executeOnContext(context -> consumer.accept(DAOProvider.getDAOFactory().getDao(context, daoClass)));
    }

    default <R, T> R callOnDAO(Class<T> daoClass, ThrowingFunction<T, R, PersistException> function) throws PersistException {
        return callOnContext(context -> function.apply(DAOProvider.getDAOFactory().getDao(context, daoClass)));
    }
}

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
    private final DerbyDAOFactory daoFactory = new DerbyDAOFactory();
    private final Class<T> daoClass;

    public DerbyDAOHelper(Class<T> daoClass) {
        this.daoClass = daoClass;
    }

    public <R> R read(ThrowingFunction<T, R, PersistException> function) throws PersistException, SQLException {
        try (Connection connection = daoFactory.getContext()) {
            T dao = daoFactory.getDao(connection, daoClass);
            return function.apply(dao);
        }
    }

    public void execute(ThrowingConsumer<T, PersistException> consumer) throws PersistException, SQLException {
        try (Connection connection = daoFactory.getContext()) {
            T dao = daoFactory.getDao(connection, daoClass);
            consumer.accept(dao);
        }
    }
}

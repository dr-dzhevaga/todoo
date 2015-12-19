package ru.todoo.dao.hibernate;

import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingConsumer;
import static ru.todoo.utils.LambdaExceptionUtil.ThrowingFunction;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateDAOUtil implements DAOUtil {
    private static final HibernateDAOUtil INSTANCE = new HibernateDAOUtil();

    private HibernateDAOUtil() {
    }

    public static HibernateDAOUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeOnContext(ThrowingConsumer<Object, PersistException> consumer) throws PersistException {

    }

    @Override
    public <R> R callOnContext(ThrowingFunction<Object, R, PersistException> function) throws PersistException {
        return null;
    }
}

package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import ru.todoo.dao.DAOProvider;
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
        Session session = null;
        try {
            session = (Session) DAOProvider.getDAOFactory().getContext();
            session.beginTransaction();
            consumer.accept(session);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new PersistException(e, e.getMessage());
        }
    }

    @Override
    public <R> R callOnContext(ThrowingFunction<Object, R, PersistException> function) throws PersistException {
        Session session = null;
        try {
            session = (Session) DAOProvider.getDAOFactory().getContext();
            session.beginTransaction();
            R result = function.apply(session);
            session.flush();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new PersistException(e, e.getMessage());
        }
    }
}

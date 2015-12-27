package ru.todoo.dao.hibernate;

import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;

import javax.persistence.EntityManager;

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
        EntityManager entityManager = (EntityManager) DAOProvider.getDAOFactory().getContext();
        try {
            entityManager.getTransaction().begin();
            consumer.accept(entityManager);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new PersistException(e, e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public <R> R callOnContext(ThrowingFunction<Object, R, PersistException> function) throws PersistException {
        EntityManager entityManager = (EntityManager) DAOProvider.getDAOFactory().getContext();
        try {
            entityManager.getTransaction().begin();
            R result = function.apply(entityManager);
            entityManager.flush();
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new PersistException(e, e.getMessage());
        } finally {
            entityManager.close();
        }
    }
}

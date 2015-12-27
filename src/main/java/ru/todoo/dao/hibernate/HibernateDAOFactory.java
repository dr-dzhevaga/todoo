package ru.todoo.dao.hibernate;

import ru.todoo.dao.*;
import ru.todoo.dao.generic.GenericDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateDAOFactory implements DAOFactory {
    private static volatile HibernateDAOFactory INSTANCE;
    private final EntityManagerFactory entityManagerFactory;
    private final Map<Class, Function<EntityManager, ? extends GenericDAO>> daoCreators = new HashMap<>();

    private HibernateDAOFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("todooJPA");
        daoCreators.put(UserDAO.class, HibernateUserDAO::new);
        daoCreators.put(CategoryDAO.class, HibernateCategoryDAO::new);
        daoCreators.put(TemplateDAO.class, HibernateTemplateDAO::new);
        daoCreators.put(TaskDAO.class, HibernateTaskDAO::new);
    }

    public static HibernateDAOFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (HibernateDAOFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HibernateDAOFactory();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Object getContext() throws PersistException {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public <R> R getDao(Object context, Class<R> daoClass) throws PersistException {
        Function<EntityManager, ?> daoCreator = daoCreators.get(daoClass);
        if (daoCreator == null) {
            throw new IllegalArgumentException("DAO for " + daoClass + " class not found");
        }
        return daoClass.cast(daoCreator.apply((EntityManager) context));
    }
}
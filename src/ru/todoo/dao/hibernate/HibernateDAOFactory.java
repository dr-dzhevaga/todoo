package ru.todoo.dao.hibernate;

import org.hibernate.Session;
import ru.todoo.dao.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public class HibernateDAOFactory implements DAOFactory {
    private static final Map<Class, Function<Session, ?>> constructors = new HashMap<>();
    private static volatile HibernateDAOFactory INSTANCE = new HibernateDAOFactory();

    static {
        constructors.put(UserDAO.class, HibernateUserDAO::new);
        constructors.put(CategoryDAO.class, HibernateCategoryDAO::new);
        constructors.put(TemplateDAO.class, HibernateTemplateDAO::new);
        constructors.put(TaskDAO.class, HibernateTaskDAO::new);
    }

    public static HibernateDAOFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Object getContext() throws PersistException {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public <R> R getDao(Object context, Class<R> daoClass) throws PersistException {
        Function<Session, ?> constructor = constructors.get(daoClass);
        if (constructor == null) {
            throw new IllegalArgumentException("DAO for " + daoClass + " class not found");
        }
        return daoClass.cast(constructor.apply((Session) context));
    }
}
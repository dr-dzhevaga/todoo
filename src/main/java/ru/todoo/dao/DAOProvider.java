package ru.todoo.dao;

import ru.todoo.dao.hibernate.HibernateDAOFactory;
import ru.todoo.dao.hibernate.HibernateDAOUtil;

/**
 * Created by Dmitriy Dzhevaga on 08.12.2015.
 */
public class DAOProvider {
    public static DAOFactory getDAOFactory() throws PersistException {
        return HibernateDAOFactory.getInstance();
    }

    public static DAOUtil getDAOUtil() {
        return HibernateDAOUtil.getInstance();
    }
}
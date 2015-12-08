package ru.todoo.dao;

import ru.todoo.dao.derby.DerbyDAOFactory;

/**
 * Created by Dmitriy Dzhevaga on 08.12.2015.
 */
public class DAOFactoryProvider {
    public static DAOFactory getDAOFactory() throws PersistException {
        return DerbyDAOFactory.getInstance();
    }
}
package ru.todoo.dao;

import ru.todoo.derby.DerbyDAOFactory;

public interface DAOFactory<T> {
    static DAOFactory getDAOFactory() {
        return new DerbyDAOFactory();
    }

    T getContext() throws PersistException;

    UserDAO getUserDao(T context) throws PersistException;
}
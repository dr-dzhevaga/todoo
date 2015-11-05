package ru.todoo.dao;

/**
 * Created by Dmitriy Dzhevaga on 03.11.2015.
 */
public interface DAOFactory<T> {
    T getContext() throws PersistException;

    UserDAO getUserDao(T context) throws PersistException;

    CategoryDAO getCategoryDAO(T context) throws PersistException;

    TaskDAO getTaskDAO(T context) throws PersistException;
}

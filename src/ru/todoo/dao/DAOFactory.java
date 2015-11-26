package ru.todoo.dao;

/**
 * Created by Dmitriy Dzhevaga on 03.11.2015.
 */
public interface DAOFactory<T> {
    T getContext() throws PersistException;

    <R> R getDao(T context, Class<R> daoClass) throws PersistException;
}

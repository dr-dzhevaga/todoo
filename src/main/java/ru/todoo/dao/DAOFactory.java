package ru.todoo.dao;

/**
 * Created by Dmitriy Dzhevaga on 03.11.2015.
 */
public interface DAOFactory {
    Object getContext() throws PersistException;

    <R> R getDao(Object context, Class<R> daoClass) throws PersistException;
}

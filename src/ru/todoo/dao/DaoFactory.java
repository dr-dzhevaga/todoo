package ru.todoo.dao;

import java.io.Serializable;

public interface DaoFactory<C> {
    C getContext() throws PersistException;
    <T extends Identified<K>, K extends Serializable> GenericDao<T, K>
        getDao(C context, Class<T> dtoClass) throws PersistException;
}
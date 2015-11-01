package ru.todoo.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Identified<K>, K extends Serializable> {
    public T create(T object)  throws PersistException;
    public T get(K key) throws PersistException;
    public void update(T object) throws PersistException;
    public void delete(T object) throws PersistException;
    public List<T> getAll() throws PersistException;
}

package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T extends Identifiable<PK>, PK extends Serializable> {
    T create(T entity) throws PersistException;

    T read(PK id) throws PersistException;

    void update(T entity) throws PersistException;

    void delete(PK id) throws PersistException;

    List<T> readAll() throws PersistException;
}
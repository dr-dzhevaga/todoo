package ru.todoo.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, PK extends Serializable> {
    T create(T entity);

    T read(PK id);

    void update(T entity);

    void delete(PK id);

    List<T> readAll();
}
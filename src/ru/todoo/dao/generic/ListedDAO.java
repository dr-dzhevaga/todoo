package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 04.11.2015.
 */
public interface ListedDAO<T extends Identified<PK> & Listed<PK>, PK extends Serializable> {
    List<T> readRoots() throws PersistException;
    List<T> readChildren(T parent) throws PersistException;
    T readLastChild(T parent) throws PersistException;
    T readFirstChild(T parent) throws PersistException;
    List<T> readChildrenRecursive(T parent) throws PersistException;
    void moveChildrenUp(T parent, Integer firstChildOrder, Integer lastChildOrder) throws PersistException;
    void moveChildrenDown(T parent, Integer firstChildOrder, Integer lastChildOrder) throws PersistException;
}

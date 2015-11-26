package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 04.11.2015.
 */
public interface ListedDAO<T extends Identified<PK> & Listed<PK>, PK extends Serializable> {
    List<T> readChildren(PK parentId) throws PersistException;

    T readLastChild(PK parentId) throws PersistException;

    T readFirstChild(PK parentId) throws PersistException;

    List<T> readChildrenRecursive(PK parentId) throws PersistException;

    void moveChildrenUp(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException;

    void moveChildrenDown(PK parentId, Integer firstChildOrder, Integer lastChildOrder) throws PersistException;
}

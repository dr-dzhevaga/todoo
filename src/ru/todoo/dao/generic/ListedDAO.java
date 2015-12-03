package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 04.11.2015.
 */
public interface ListedDAO<T extends Identified<PK> & Listed<PK>, PK extends Serializable> {
    List<T> readHierarchy(PK parentId) throws PersistException;
}

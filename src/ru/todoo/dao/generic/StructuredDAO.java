package ru.todoo.dao.generic;

import ru.todoo.dao.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 04.11.2015.
 */
public interface StructuredDAO<T extends Identified<PK> & Structured<PK>, PK extends Serializable> {
    List<T> readStructure(PK parentId) throws PersistException;

    void deleteStructure(PK id) throws PersistException;
}

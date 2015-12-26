package ru.todoo.dao.hibernate;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.hibernate.Session;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.dao.generic.Identifiable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public abstract class HibernateGenericDAO<T extends Identifiable<PK>, PK extends Serializable> implements GenericDAO<T, PK> {
    protected final Class<T> type;
    protected final Session session;

    public HibernateGenericDAO(Class<T> type, Session session) {
        this.type = type;
        this.session = session;
    }

    @Override
    public T create(T entity) throws PersistException {
        session.persist(entity);
        return entity;
    }

    @Override
    public T read(PK id) throws PersistException {
        return session.get(type, id);
    }

    @Override
    public void update(T updatedEntity) throws PersistException {
        T originEntity = session.get(type, updatedEntity.getId());
        DozerBeanMapperSingletonWrapper.getInstance().map(updatedEntity, originEntity);
    }

    @Override
    public void delete(PK id) throws PersistException {
        T entity = session.get(type, id);
        session.delete(entity);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<T> readAll() throws PersistException {
        return session.createCriteria(type).list();
    }
}

package ru.todoo.dao.hibernate;

import org.dozer.DozerBeanMapperSingletonWrapper;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.dao.generic.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public abstract class HibernateGenericDAO<T extends Identifiable<PK>, PK extends Serializable> implements GenericDAO<T, PK> {
    protected final Class<T> type;
    protected final EntityManager entityManager;

    public HibernateGenericDAO(Class<T> type, EntityManager entityManager) {
        this.type = type;
        this.entityManager = entityManager;
    }

    @Override
    public T create(T entity) throws PersistException {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T read(PK id) throws PersistException {
        return entityManager.find(type, id);
    }

    @Override
    public void update(T updatedEntity) throws PersistException {
        T originEntity = entityManager.find(type, updatedEntity.getId());
        DozerBeanMapperSingletonWrapper.getInstance().map(updatedEntity, originEntity);
    }

    @Override
    public void delete(PK id) throws PersistException {
        T entity = entityManager.find(type, id);
        entityManager.remove(entity);
    }

    @Override
    public List<T> readAll() throws PersistException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> entity = criteriaQuery.from(type);
        criteriaQuery.select(entity);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
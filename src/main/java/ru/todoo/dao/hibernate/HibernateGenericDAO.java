package ru.todoo.dao.hibernate;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.todoo.dao.GenericDAO;
import ru.todoo.domain.entity.IdentifiableEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
public abstract class HibernateGenericDAO<T extends IdentifiableEntity, PK extends Serializable> implements GenericDAO<T, PK> {
    protected final Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    Mapper mapper;

    public HibernateGenericDAO(Class<T> type) {
        this.type = type;
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T read(PK id) {
        return entityManager.find(type, id);
    }

    @Override
    public void update(T updatedEntity) {
        T originEntity = entityManager.find(type, updatedEntity.getId());
        // TODO: move it to service layer?
        mapper.map(updatedEntity, originEntity);
    }

    @Override
    public void delete(PK id) {
        T entity = entityManager.find(type, id);
        entityManager.remove(entity);
    }

    @Override
    public List<T> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> entity = criteriaQuery.from(type);
        criteriaQuery.select(entity);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
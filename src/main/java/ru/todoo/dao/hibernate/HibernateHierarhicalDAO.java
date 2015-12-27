package ru.todoo.dao.hibernate;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.generic.Hierarchical;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Dzhevaga Dmitriy on 23.12.2015.
 */
public abstract class HibernateHierarhicalDAO<T extends Hierarchical<PK>, PK extends Serializable> extends HibernateGenericDAO<T, PK> {
    public HibernateHierarhicalDAO(Class<T> type, EntityManager entityManager) {
        super(type, entityManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T create(T entity) throws PersistException {
        if (entity.getParent() != null) {
            Hierarchical parentEntity = entityManager.find(type, entity.getParent().getId());
            parentEntity.getChildren().add(entity);
            return entity;
        } else {
            return super.create(entity);
        }
    }

    @Override
    public void delete(PK id) throws PersistException {
        Hierarchical entity = entityManager.find(type, id);
        if (entity.getParent() != null) {
            Hierarchical parentEntity = entityManager.find(type, entity.getParent().getId());
            parentEntity.getChildren().remove(entity);
        } else {
            super.delete(id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(T updatedEntity) throws PersistException {
        Hierarchical originEntity = entityManager.find(type, updatedEntity.getId());
        if ((updatedEntity.getParent() != null) && (originEntity.getParent() != null)) {
            if (!Objects.equals(updatedEntity.getParent().getId(), originEntity.getParent().getId())) {
                Hierarchical newParentEntity = entityManager.find(type, updatedEntity.getParent().getId());
                newParentEntity.getChildren().add(originEntity);
            } else if (!Objects.equals(updatedEntity.getOrder(), originEntity.getOrder())) {
                Hierarchical parentEntity = originEntity.getParent();
                parentEntity.getChildren().remove(originEntity);
                parentEntity.getChildren().add(updatedEntity.getOrder(), originEntity);
            } else {
                super.update(updatedEntity);
            }
        } else {
            super.update(updatedEntity);
        }
    }
}

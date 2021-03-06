package ru.todoo.dao.hibernate;

import ru.todoo.domain.entity.HierarchicalEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Dzhevaga Dmitriy on 23.12.2015.
 */
public abstract class HibernateHierarchicalDAO<T extends HierarchicalEntity<T, PK>, PK extends Serializable> extends HibernateGenericDAO<T, PK> {
    public HibernateHierarchicalDAO(Class<T> type) {
        super(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T create(T entity) {
        if (entity.getParent() != null) {
            T parentEntity = entityManager.find(type, entity.getParent().getId());
            parentEntity.getChildren().add(entity);
            return entity;
        } else {
            return super.create(entity);
        }
    }

    @Override
    public void delete(PK id) {
        T entity = entityManager.find(type, id);
        if (entity.getParent() != null) {
            T parentEntity = entityManager.find(type, entity.getParent().getId());
            parentEntity.getChildren().remove(entity);
        } else {
            super.delete(id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(T updatedEntity) {
        T originEntity = entityManager.find(type, updatedEntity.getId());
        if ((updatedEntity.getParent() != null) && (originEntity.getParent() != null)) {
            if (!Objects.equals(updatedEntity.getParent().getId(), originEntity.getParent().getId())) {
                T newParentEntity = entityManager.find(type, updatedEntity.getParent().getId());
                newParentEntity.getChildren().add(originEntity);
            } else if (!Objects.equals(updatedEntity.getOrder(), originEntity.getOrder())) {
                T parentEntity = originEntity.getParent();
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

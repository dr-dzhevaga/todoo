package ru.todoo.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 15.01.2016.
 */
@MappedSuperclass
public abstract class HierarchicalEntity<T extends HierarchicalEntity<T, PK>, PK extends Serializable> extends IdentifiableEntity<PK> {
    @Basic
    @Column(name = "ORDER_NUMBER")
    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public abstract List<T> getChildren();

    public abstract void setChildren(List<T> children);

    public abstract T getParent();

    public abstract void setParent(T parent);
}

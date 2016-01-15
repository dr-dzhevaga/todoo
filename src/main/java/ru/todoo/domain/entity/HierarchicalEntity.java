package ru.todoo.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 15.01.2016.
 */
@MappedSuperclass
public class HierarchicalEntity<T extends HierarchicalEntity<T, PK>, PK extends Serializable> extends IdentifiableEntity<PK> {
    @Basic
    @Column(name = "ORDER_NUMBER")
    private Integer order;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "ORDER_NUMBER")
    private List<T> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private T parent;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }
}

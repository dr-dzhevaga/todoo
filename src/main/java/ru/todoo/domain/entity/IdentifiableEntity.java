package ru.todoo.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dmitriy Dzhevaga on 15.01.2016.
 */
@MappedSuperclass
public abstract class IdentifiableEntity<PK extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    protected PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public boolean isNew() {
        return (id == null);
    }
}
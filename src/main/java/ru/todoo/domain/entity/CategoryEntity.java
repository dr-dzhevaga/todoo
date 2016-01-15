package ru.todoo.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Dmitriy Dzhevaga on 17.12.2015.
 */
@Entity
@Table(name = "CATEGORIES", schema = "APP")
public class CategoryEntity extends IdentifiableEntity<Integer> {
    @Basic
    @Column(name = "NAME")
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

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
    private String name;

    @Basic
    @Column(name = "FILTER")
    private String filter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

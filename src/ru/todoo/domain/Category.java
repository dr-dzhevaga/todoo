package ru.todoo.domain;

import ru.todoo.dao.generic.Identified;

/**
 * Created by Dmitriy Dzhevaga on 28.10.2015.
 */
public class Category implements Identified<Integer> {
    private Integer id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

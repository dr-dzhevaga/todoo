package ru.todoo.domain;

/**
 * Created by Dmitriy Dzhevaga on 28.10.2015.
 */
public class Category {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

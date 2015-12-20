package ru.todoo.domain.dto;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class CategoryDTO {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

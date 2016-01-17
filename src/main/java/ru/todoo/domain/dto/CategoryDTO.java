package ru.todoo.domain.dto;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class CategoryDTO {
    private Integer id;
    private String name;
    private String filter;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

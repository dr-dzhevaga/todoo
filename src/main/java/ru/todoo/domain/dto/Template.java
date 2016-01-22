package ru.todoo.domain.dto;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class Template extends BaseTask {
    private Integer categoryId;
    private List<Template> children;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Template> getChildren() {
        return children;
    }

    public void setChildren(List<Template> children) {
        this.children = children;
    }
}

package ru.todoo.domain;

import ru.todoo.dao.generic.Identified;
import ru.todoo.dao.generic.Listed;

import java.sql.Timestamp;

/**
 * Created by Dmitriy Dzhevaga on 28.10.2015.
 */
public class Task implements Identified<Integer>, Listed<Integer> {
    private Integer id;
    private Integer parentId;
    private Integer order;
    private String name;
    private String description;
    private boolean template;
    private Integer categoryId;
    private Integer userId;
    private Integer originId;
    private boolean completed;
    private Timestamp created;
    private Timestamp modified;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getCreated() {
        return created;
    }

    protected void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}

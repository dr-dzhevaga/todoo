package ru.todoo.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 17.12.2015.
 */
@Entity
@Table(name = "TASKS", schema = "APP", catalog = "")
public class TaskEntity {
    private int id;
    private Integer order;
    private String name;
    private String description;
    private UserEntity user;
    private Boolean isTemplate;
    private CategoryEntity category;
    private TaskEntity origin;
    private boolean isCompleted;
    private Timestamp created;
    private Timestamp modified;
    private List<TaskEntity> children = new ArrayList<>();
    private TaskEntity parent;

    @Id
    @GeneratedValue
    @Column(name = "ID", insertable = false, updatable = false)
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ORDER_NUMBER")
    @OrderColumn
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "IS_TEMPLATE", updatable = false)
    public Boolean getTemplate() {
        return isTemplate;
    }

    private void setTemplate(Boolean template) {
        isTemplate = template;
    }

    @Basic
    @Column(name = "IS_COMPLETED")
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Basic
    @Column(name = "CREATED", insertable = false, updatable = false)
    public Timestamp getCreated() {
        return created;
    }

    private void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "MODIFIED", updatable = false)
    public Timestamp getModified() {
        return modified;
    }

    private void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @PreUpdate
    private void updateModified() {
        this.modified = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    public List<TaskEntity> getChildren() {
        return children;
    }

    private void setChildren(List<TaskEntity> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public TaskEntity getParent() {
        return parent;
    }

    public void setParent(TaskEntity parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    public TaskEntity getOrigin() {
        return origin;
    }

    private void setOrigin(TaskEntity origin) {
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskEntity that = (TaskEntity) o;

        if (id != that.id) return false;
        if (isCompleted != that.isCompleted) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (isTemplate != null ? !isTemplate.equals(that.isTemplate) : that.isTemplate != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        return !(modified != null ? !modified.equals(that.modified) : that.modified != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isTemplate != null ? isTemplate.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }
}

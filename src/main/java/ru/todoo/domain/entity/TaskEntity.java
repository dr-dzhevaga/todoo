package ru.todoo.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "0")
public class TaskEntity extends BaseTaskEntity<TaskEntity, Integer> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private TemplateEntity origin;

    @Basic
    @Column(name = "IS_COMPLETED")
    private boolean isCompleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "ORDER_NUMBER")
    private List<TaskEntity> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private TaskEntity parent;

    public TemplateEntity getOrigin() {
        return origin;
    }

    public void setOrigin(TemplateEntity origin) {
        this.origin = origin;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public List<TaskEntity> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TaskEntity> children) {
        this.children = children;
    }

    @Override
    public TaskEntity getParent() {
        return parent;
    }

    @Override
    public void setParent(TaskEntity parent) {
        this.parent = parent;
    }
}

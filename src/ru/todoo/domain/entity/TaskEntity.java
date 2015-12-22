package ru.todoo.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "0")
public class TaskEntity extends AbstractTaskEntity {
    private TemplateEntity origin;
    private boolean isCompleted;
    private List<TaskEntity> children = new ArrayList<>();
    private TaskEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    public TemplateEntity getOrigin() {
        return origin;
    }

    public void setOrigin(TemplateEntity origin) {
        this.origin = origin;
    }

    @Basic
    @Column(name = "IS_COMPLETED")
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "ORDER_NUMBER")
    public List<TaskEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TaskEntity> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public TaskEntity getParent() {
        return parent;
    }

    public void setParent(TaskEntity parent) {
        this.parent = parent;
    }
}

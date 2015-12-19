package ru.todoo.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "0")
public class Task extends AbstractTask {
    private Template origin;
    private boolean isCompleted;
    private List<Task> children = new ArrayList<>();
    private Task parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    public Template getOrigin() {
        return origin;
    }

    public void setOrigin(Template origin) {
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
    public List<Task> getChildren() {
        return children;
    }

    private void setChildren(List<Task> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

}

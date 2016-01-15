package ru.todoo.domain.entity;

import javax.persistence.*;

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
}

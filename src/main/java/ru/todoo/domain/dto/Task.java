package ru.todoo.domain.dto;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class Task extends BaseTask {
    private Integer originId;
    private boolean isCompleted;
    private List<Task> children;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }
}

package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.domain.Task;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService extends TaskServiceAbstract {
    public Task create(Task task) throws PersistException {
        task.setTemplate(false);
        return super.create(task);
    }

    public void updateStatus(Integer taskId, boolean completed) throws PersistException {
        update(taskId, task -> task.setCompleted(completed));
    }
}
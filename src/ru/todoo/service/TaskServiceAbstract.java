package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public abstract class TaskServiceAbstract {
    protected final DerbyDAOHelper<TaskDAO> daoHelper = new DerbyDAOHelper<>(TaskDAO.class);

    protected Task create(Task task) throws PersistException {
        return daoHelper.executeFunction(taskDAO -> taskDAO.create(task));
    }

    public Task read(Integer taskId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.read(taskId));
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.readHierarchy(parentId));
    }

    public void delete(Integer taskId) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> taskDAO.delete(taskId));
    }

    public void update(Task task) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> taskDAO.update(task));
    }
}

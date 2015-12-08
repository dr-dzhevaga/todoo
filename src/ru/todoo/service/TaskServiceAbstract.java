package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public abstract class TaskServiceAbstract {
    protected final DerbyDAOHelper<TaskDAO> daoHelper;

    protected TaskServiceAbstract() throws PersistException {
        daoHelper = new DerbyDAOHelper<>(TaskDAO.class);
    }

    protected Task create(Task task) throws PersistException {
        return daoHelper.callOnDAO(taskDAO -> taskDAO.create(task), true);
    }

    public Task read(Integer taskId) throws PersistException {
        return daoHelper.callOnDAO(taskDAO -> taskDAO.read(taskId));
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return daoHelper.callOnDAO(taskDAO -> taskDAO.readHierarchy(parentId));
    }

    public void delete(Integer taskId) throws PersistException {
        daoHelper.executeOnDAO(taskDAO -> taskDAO.delete(taskId), true);
    }

    public void update(Task task) throws PersistException {
        daoHelper.executeOnDAO(taskDAO -> taskDAO.update(task), true);
    }
}

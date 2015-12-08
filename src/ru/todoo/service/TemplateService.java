package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    protected final DerbyDAOHelper<TaskDAO> daoHelper;

    public TemplateService() throws PersistException {
        daoHelper = new DerbyDAOHelper<>(TaskDAO.class);
    }

    public List<Task> readAll() throws PersistException {
        return daoHelper.callOnDAO(TaskDAO::readAllTaskTemplates);
    }

    public List<Task> readByCategory(Integer categoryId) throws PersistException {
        return daoHelper.callOnDAO(taskDAO -> taskDAO.readTaskTemplatesByCategory(categoryId));
    }

    public List<Task> readPopular() throws PersistException {
        return daoHelper.callOnDAO(TaskDAO::readPopularTaskTemplates);
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return daoHelper.callOnDAO(taskDAO -> taskDAO.readHierarchy(parentId));
    }

    public Task create(Task task) throws PersistException {
        task.setTemplate(true);
        return daoHelper.callOnDAO(taskDAO -> taskDAO.create(task), true);
    }

    public void delete(Integer taskId) throws PersistException {
        daoHelper.executeOnDAO(taskDAO -> taskDAO.delete(taskId), true);
    }

    public void update(Task task) throws PersistException {
        daoHelper.executeOnDAO(taskDAO -> taskDAO.update(task), true);
    }
}

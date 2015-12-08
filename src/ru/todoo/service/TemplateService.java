package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    public List<Task> readAll() throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, TaskDAO::readAllTaskTemplates);
    }

    public List<Task> readByCategory(Integer categoryId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> taskDAO.readTaskTemplatesByCategory(categoryId));
    }

    public List<Task> readPopular() throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, TaskDAO::readPopularTaskTemplates);
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> taskDAO.readStructure(parentId));
    }

    public Task create(Task task) throws PersistException {
        task.setTemplate(true);
        return DAOHelper.callOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.create(task));
    }

    public void delete(Integer taskId) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.deleteStructure(taskId));
    }

    public void update(Task task) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.update(task));
    }
}

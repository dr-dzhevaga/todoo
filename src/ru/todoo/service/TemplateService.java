package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService extends TaskServiceAbstract {
    public List<Task> readAll() throws PersistException {
        return daoHelper.read(TaskDAO::readAllTaskTemplates);
    }

    public List<Task> readByCategory(Integer categoryId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.readTaskTemplatesByCategory(categoryId));
    }

    public List<Task> readPopular() throws PersistException {
        return daoHelper.read(TaskDAO::readPopularTaskTemplates);
    }

    public Task create(Task task) throws PersistException {
        task.setTemplate(true);
        return super.create(task);
    }
}

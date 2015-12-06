package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.domain.Task;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService extends TaskServiceAbstract {
    protected TaskService() throws PersistException {
    }

    public List<Task> readByUser(Integer userId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.readTasksByUser(userId));
    }

    @Override
    public Task create(Task task) throws PersistException {
        task.setTemplate(false);
        return super.create(task);
    }

    public Task createFromTemplate(Integer templateId, Integer userId) throws PersistException {
        List<Task> templates = readHierarchy(templateId);
        if (templates.isEmpty()) {
            throw new PersistException("Template " + templateId + " is not found");
        }
        return createRecursive(templates, userId, null, null);
    }

    private Task createRecursive(List<Task> templates, Integer userId, Integer templateParentId, Integer taskParentId) throws PersistException {
        Task task = null;
        for (Task template : filterByParent(templates, templateParentId)) {
            template.setUserId(userId);
            template.setParentId(taskParentId);
            task = create(template);
            createRecursive(templates, userId, template.getId(), task.getId());
        }
        return task;
    }

    private List<Task> filterByParent(List<Task> templates, Integer parentId) {
        return templates.stream().filter(task -> Objects.equals(parentId, task.getParentId())).collect(Collectors.toList());
    }
}
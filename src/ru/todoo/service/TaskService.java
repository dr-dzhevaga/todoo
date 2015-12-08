package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
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
        return daoHelper.callOnDAO(taskDAO -> taskDAO.readTasksByUser(userId));
    }

    @Override
    public Task create(Task task) throws PersistException {
        task.setTemplate(false);
        return super.create(task);
    }

    public Task createFromTemplate(Integer templateId, Integer userId) throws PersistException {
        return daoHelper.callOnDAO(
                taskDAO -> {
                    List<Task> templates = taskDAO.readHierarchy(templateId);
                    if (templates.isEmpty()) {
                        throw new PersistException("Template " + templateId + " is not found");
                    }
                    return createFromTemplatesRecursive(templates, userId, null, null, taskDAO);
                },
                true
        );
    }

    private Task createFromTemplatesRecursive(List<Task> templates, Integer userId, Integer templateId, Integer taskId, TaskDAO taskDAO) throws PersistException {
        Task task = null;
        for (Task template : filterByParent(templates, templateId)) {
            template.setTemplate(false);
            template.setUserId(userId);
            template.setParentId(taskId);
            task = taskDAO.create(template);
            createFromTemplatesRecursive(templates, userId, template.getId(), task.getId(), taskDAO);
        }
        return task;
    }

    private List<Task> filterByParent(List<Task> templates, Integer parentId) {
        return templates.stream().filter(task -> Objects.equals(parentId, task.getParentId())).collect(Collectors.toList());
    }
}
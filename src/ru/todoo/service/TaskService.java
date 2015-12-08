package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;
import ru.todoo.domain.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService {
    private static final String ACCESS_ERROR = "Access denied";
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";
    private final User user;

    public TaskService(String username) throws PersistException {
        user = ServiceProvider.getUserService().readByLogin(username);
    }

    public List<Task> readAll() throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> taskDAO.readTasksByUser(user.getId()));
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> {
            Task parent = taskDAO.read(parentId);
            checkOwner(parent);
            return taskDAO.readStructure(parentId);
        });
    }

    public Task create(Task task) throws PersistException {
        task.setTemplate(false);
        task.setUserId(user.getId());
        return DAOHelper.callOnDAO(TaskDAO.class, true, taskDAO -> {
            if (task.getParentId() != null) {
                Task parent = taskDAO.read(task.getParentId());
                checkOwner(parent);
            }
            return taskDAO.create(task);
        });
    }

    public Task createFromTemplate(Integer templateId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, true, taskDAO -> {
            List<Task> templates = taskDAO.readStructure(templateId);
            if (templates.isEmpty()) {
                throw new PersistException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, templateId));
            }
            return createFromTemplatesRecursive(templates, null, null, taskDAO);
        });
    }

    private Task createFromTemplatesRecursive(List<Task> templates, Integer templateId, Integer taskId, TaskDAO taskDAO) throws PersistException {
        Task task = null;
        for (Task template : getChildren(templates, templateId)) {
            template.setTemplate(false);
            template.setUserId(user.getId());
            template.setParentId(taskId);
            task = taskDAO.create(template);
            createFromTemplatesRecursive(templates, template.getId(), task.getId(), taskDAO);
        }
        return task;
    }

    private List<Task> getChildren(List<Task> templates, Integer parentId) {
        return templates.stream().filter(task -> Objects.equals(parentId, task.getParentId())).collect(Collectors.toList());
    }

    public void delete(Integer taskId) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> {
            Task task = taskDAO.read(taskId);
            checkOwner(task);
            taskDAO.deleteStructure(taskId);
        });
    }

    public void update(Task task) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> {
            checkOwner(task);
            taskDAO.update(task);
        });
    }

    private void checkOwner(Task task) {
        if (!Objects.equals(task.getUserId(), user.getId())) {
            throw new SecurityException(ACCESS_ERROR);
        }
    }
}
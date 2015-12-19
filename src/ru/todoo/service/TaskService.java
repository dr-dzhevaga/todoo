package ru.todoo.service;

import ru.todoo.dao.*;
import ru.todoo.domain.Task;
import ru.todoo.domain.Template;
import ru.todoo.domain.User;

import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService {
    private static final String ACCESS_ERROR = "Access denied";
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private final User user;

    public TaskService(String username) throws PersistException {
        user = ServiceProvider.getUserService().readByLogin(username);
    }

    public List<Task> readAll() throws PersistException {
        return daoUtil.callOnDAO(TaskDAO.class, taskDAO -> taskDAO.readByUser(user.getId()));
    }

    public Task read(Integer id) throws PersistException {
        return daoUtil.callOnDAO(TaskDAO.class, taskDAO -> {
            Task task = taskDAO.read(id);
            checkOwner(task);
            return task;
        });
    }

    public Task create(Task task) throws PersistException {
        task.setUser(user);
        return daoUtil.callOnDAO(TaskDAO.class, taskDAO -> {
            if (task.getParent() != null) {
                Task parent = taskDAO.read(task.getParent().getId());
                checkOwner(parent);
            }
            return taskDAO.create(task);
        });
    }

    public Task createFromTemplate(Integer templateId) throws PersistException {
        return daoUtil.callOnContext(session -> {
            TemplateDAO templateDAO = DAOProvider.getDAOFactory().getDao(session, TemplateDAO.class);
            TaskDAO taskDAO = DAOProvider.getDAOFactory().getDao(session, TaskDAO.class);
            Template template = templateDAO.read(templateId);
            if (template == null) {
                throw new PersistException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, templateId));
            }
            Task task = createTaskFromTemplate(template, user);
            return taskDAO.create(task);
        });
    }

    private Task createTaskFromTemplate(Template template, User user) {
        Task task = new Task();
        task.setUser(user);
        task.setOrigin(template);
        task.setName(template.getName());
        task.setDescription(template.getDescription());
        task.setOrder(template.getOrder());
        template.getChildren().forEach(childTemplate ->
                task.getChildren().add(createTaskFromTemplate(childTemplate, user)));
        return task;
    }

    public void delete(Integer taskId) throws PersistException {
        daoUtil.executeOnDAO(TaskDAO.class, taskDAO -> {
            Task task = taskDAO.read(taskId);
            checkOwner(task);
            taskDAO.delete(taskId);
        });
    }

    public void update(Task task) throws PersistException {
        daoUtil.executeOnDAO(TaskDAO.class, taskDAO -> {
            checkOwner(task);
            taskDAO.update(task);
        });
    }

    private void checkOwner(Task task) {
        if (!Objects.equals(task.getUser(), user)) {
            throw new SecurityException(ACCESS_ERROR);
        }
    }
}
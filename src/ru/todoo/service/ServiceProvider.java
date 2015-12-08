package ru.todoo.service;

import ru.todoo.dao.PersistException;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class ServiceProvider {
    public UserService getUserService() throws PersistException {
        return new UserService();
    }

    public CategoryService getCategoryService() throws PersistException {
        return new CategoryService();
    }

    public TaskService getTaskService(String username) throws PersistException {
        return new TaskService(username);
    }

    public TemplateService getTemplateService() throws PersistException {
        return new TemplateService();
    }
}

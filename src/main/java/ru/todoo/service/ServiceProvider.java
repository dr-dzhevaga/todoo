package ru.todoo.service;

import ru.todoo.dao.PersistException;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class ServiceProvider {
    private ServiceProvider() {
    }

    public static UserService getUserService() throws PersistException {
        return new UserService();
    }

    public static CategoryService getCategoryService() throws PersistException {
        return new CategoryService();
    }

    public static TaskService getTaskService(String username) throws PersistException {
        return new TaskService(username);
    }

    public static TemplateService getTemplateService() throws PersistException {
        return new TemplateService();
    }
}

package ru.todoo.service;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class ServiceProvider {
    public UserService getUserService() {
        return new UserService();
    }

    public CategoryService getCategoryService() {
        return new CategoryService();
    }

    public TaskService getTaskService() {
        return new TaskService();
    }
}

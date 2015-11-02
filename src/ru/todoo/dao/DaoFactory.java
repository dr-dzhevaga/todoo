package ru.todoo.dao;

public interface DAOFactory<T> {
    T getContext() throws PersistException;

    UserDAO getUserDao(T context) throws PersistException;

    CategoryDAO getCategoryDAO(T context) throws PersistException;

    TemplateDAO getTemplateDAO(T context) throws PersistException;
}
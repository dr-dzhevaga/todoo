package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface TaskDAO extends GenericDAO<Task, Integer> {
    List<Task> readTaskTemplatesByCategory(Integer categoryId) throws PersistException;

    List<Task> readPopularTaskTemplates() throws PersistException;

    List<Task> readTasksByUser(Integer userId) throws PersistException;
}

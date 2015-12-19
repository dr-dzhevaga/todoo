package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Task;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface TaskDAO extends GenericDAO<Task, Integer> {
    List<Task> readByUser(Integer userId) throws PersistException;
}

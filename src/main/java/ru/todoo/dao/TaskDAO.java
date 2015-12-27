package ru.todoo.dao;

import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.entity.TaskEntity;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 02.11.2015.
 */
public interface TaskDAO extends GenericDAO<TaskEntity, Integer> {
    List<TaskEntity> readRootByUser(Integer userId) throws PersistException;
}

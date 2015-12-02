package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public abstract class TaskServiceAbstract {
    protected final DerbyDAOHelper<TaskDAO> daoHelper = new DerbyDAOHelper<>(TaskDAO.class);

    protected Task create(Task task) throws PersistException {
        return daoHelper.executeFunction(taskDAO -> {
            if (task.getParentId() != null) {
                // limit hierarchy level by 2
                Task parent = taskDAO.read(task.getParentId());
                if (parent.getParentId() != null) {
                    Task grandParent = taskDAO.read(parent.getParentId());
                    if (grandParent.getParentId() != null) {
                        task.setParentId(parent.getParentId());
                    }
                }
                // set new task to the end of the list of children
                Task lastNeighbor = taskDAO.readLastChild(task.getParentId());
                if (lastNeighbor != null) {
                    task.setOrder(lastNeighbor.getOrder() + 1);
                } else {
                    task.setOrder(0);
                }
            }
            return taskDAO.create(task);
        });
    }

    public Task read(Integer taskId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.read(taskId));
    }

    public List<Task> readChildren(Integer parentId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.readChildrenRecursive(parentId));
    }

    public void delete(Integer taskId) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> {
            Task task = taskDAO.read(taskId);
            if (task.getParentId() != null) {
                Task lastNeighbor = taskDAO.readLastChild(task.getParentId());
                if (lastNeighbor != null && !Objects.equals(lastNeighbor.getId(), taskId)) {
                    taskDAO.moveChildrenUp(task.getParentId(), task.getOrder() + 1, lastNeighbor.getOrder());
                }
            }
            taskDAO.delete(taskId);
        });
    }

    public void update(Task task) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> taskDAO.update(task));
    }

    public void updateOrder(Integer taskId, Integer order) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> {
            Task task = taskDAO.read(taskId);
            if (order > task.getOrder()) {
                taskDAO.moveChildrenUp(task.getParentId(), task.getOrder() + 1, order);
            } else if (order < task.getOrder()) {
                taskDAO.moveChildrenDown(task.getParentId(), order, task.getOrder() - 1);
            }
            task.setOrder(order);
            taskDAO.update(task);
        });
    }

    public void updateParent(Integer taskId, Integer parentId) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> {
            Task task = taskDAO.read(taskId);
            Integer oldParentTaskId = task.getParentId();
            if (oldParentTaskId != null) {
                Task oldLastNeighbor = taskDAO.readLastChild(oldParentTaskId);
                if (oldLastNeighbor != null) {
                    taskDAO.moveChildrenUp(oldParentTaskId, task.getOrder() + 1, oldLastNeighbor.getOrder());
                }
            }
            Task lastNeighbor = taskDAO.readLastChild(parentId);
            if (lastNeighbor != null) {
                task.setOrder(lastNeighbor.getOrder() + 1);
            } else {
                task.setOrder(0);
            }
            task.setParentId(parentId);
            taskDAO.update(task);
        });
    }
}

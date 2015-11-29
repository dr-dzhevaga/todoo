package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public abstract class TaskServiceAbstract {
    protected final DerbyDAOHelper<TaskDAO> daoHelper = new DerbyDAOHelper<>(TaskDAO.class);

    protected Task create(Task task) throws PersistException {
        return daoHelper.executeFunction(taskDAO -> {
            Integer parentTaskId = task.getParentId();
            if (parentTaskId != null) {
                Task lastNeighbor = taskDAO.readLastChild(parentTaskId);
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

    public List<Task> readByUser(Integer userId) throws PersistException {
        return daoHelper.read(taskDAO -> taskDAO.readTasksByUser(userId));
    }

    public void delete(Integer taskId) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> {
            Task task = taskDAO.read(taskId);
            Integer parentTaskId = task.getParentId();
            if (parentTaskId != null) {
                Task lastNeighbor = taskDAO.readLastChild(parentTaskId);
                if (lastNeighbor != null && !Objects.equals(lastNeighbor.getId(), taskId)) {
                    taskDAO.moveChildrenUp(task.getParentId(), task.getOrder() + 1, lastNeighbor.getOrder());
                }
            }
            taskDAO.delete(taskId);
        });
    }

    protected void update(Integer taskId, Consumer<Task> updater) throws PersistException {
        daoHelper.executeProcedure(taskDAO -> {
            Task task = taskDAO.read(taskId);
            updater.accept(task);
            taskDAO.update(task);
        });
    }

    public void updateName(Integer taskId, String name) throws PersistException {
        update(taskId, task -> task.setName(name));
    }

    public void updateDescription(Integer taskId, String description) throws PersistException {
        update(taskId, task -> task.setDescription(description));
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

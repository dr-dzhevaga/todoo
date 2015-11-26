package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService {
    private final DerbyDAOHelper<TaskDAO> daoHelper = new DerbyDAOHelper<>(TaskDAO.class);

    public void addTaskTemplate(String name, String description, Integer categoryId, Integer userId) throws PersistException, SQLException {
        addTask(null, name, description, true, categoryId, userId);
    }

    public void addTaskTemplate(Integer parentId, String name, String description, Integer categoryId, Integer userId) throws PersistException, SQLException {
        addTask(parentId, name, description, true, categoryId, userId);
    }

    public void addTask(String name, String description, Integer categoryId, Integer userId) throws PersistException, SQLException {
        addTask(null, name, description, false, categoryId, userId);
    }

    public void addTask(Integer parentId, String name, String description, Integer categoryId, Integer userId) throws PersistException, SQLException {
        addTask(parentId, name, description, false, categoryId, userId);
    }

    private void addTask(Integer parentId, String name, String description, boolean isTemplate, Integer categoryId, Integer userId) throws PersistException, SQLException {
        Task task = new Task();
        task.setTemplate(isTemplate);
        task.setParentId(parentId);
        task.setName(name);
        task.setDescription(description);
        task.setCategoryId(categoryId);
        task.setUserId(userId);
        addTask(task);
    }

    private void addTask(Task task) throws PersistException, SQLException {
        daoHelper.execute(taskDAO -> {
            Integer parentTaskId = task.getParentId();
            if (parentTaskId != null) {
                Task lastNeighbor = taskDAO.readLastChild(parentTaskId);
                if (lastNeighbor != null) {
                    task.setOrder(lastNeighbor.getOrder() + 1);
                } else {
                    task.setOrder(0);
                }
            }
            taskDAO.create(task);
        });
    }

    public Task getTask(Integer taskId) throws PersistException, SQLException {
        return daoHelper.read(taskDAO -> taskDAO.read(taskId));
    }

    public List<Task> getTasksByUser(Integer userId) throws PersistException, SQLException {
        return daoHelper.read(taskDAO -> taskDAO.readTasksByUser(userId));
    }

    public List<Task> getTaskTemplatesByCategory(Integer categoryId) throws PersistException, SQLException {
        return daoHelper.read(taskDAO -> taskDAO.readTaskTemplatesByCategory(categoryId));
    }

    public List<Task> getPopularTaskTemplates() throws PersistException, SQLException {
        return daoHelper.read(TaskDAO::readPopularTaskTemplates);
    }

    public void deleteTask(Integer taskId) throws PersistException, SQLException {
        daoHelper.execute(taskDAO -> {
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

    private void changeTask(Integer taskId, Consumer<Task> changer) throws PersistException, SQLException {
        daoHelper.execute(taskDAO -> {
            Task task = taskDAO.read(taskId);
            changer.accept(task);
            taskDAO.update(task);
        });
    }

    public void changeTaskStatus(Integer taskId, boolean completed) throws PersistException, SQLException {
        changeTask(taskId, task -> task.setCompleted(completed));
    }

    public void changeTaskName(Integer taskId, String name) throws PersistException, SQLException {
        changeTask(taskId, task -> task.setName(name));
    }

    public void changeTaskDescription(Integer taskId, String description) throws PersistException, SQLException {
        changeTask(taskId, task -> task.setDescription(description));
    }

    public void changeTaskOrder(Integer taskId, Integer order) throws PersistException, SQLException {
        daoHelper.execute(taskDAO -> {
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

    public void changeTaskParent(Integer taskId, Integer parentId) throws PersistException, SQLException {
        daoHelper.execute(taskDAO -> {
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

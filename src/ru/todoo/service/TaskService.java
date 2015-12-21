package ru.todoo.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.*;
import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.TaskEntity;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService {
    private static final String ACCESS_ERROR = "Access denied";
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
    private final UserDTO user;

    public TaskService(String username) throws PersistException {
        user = ServiceProvider.getUserService().readByLogin(username);
    }

    public List<TaskDTO> readAll() throws PersistException {
        return daoUtil.call(TaskDAO.class, taskDAO -> {
            List<TaskEntity> tasks = taskDAO.readRootByUser(user.getId());
            return tasks.stream().
                    map(task -> mapper.map(task, TaskDTO.class, "taskWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public TaskDTO read(Integer id) throws PersistException {
        return daoUtil.call(TaskDAO.class, taskDAO -> {
            TaskEntity task = taskDAO.read(id);
            checkOwner(task);
            return mapper.map(task, TaskDTO.class);
        });
    }

    public TaskDTO create(TaskDTO task) throws PersistException {
        return daoUtil.call(TaskDAO.class, taskDAO -> {
            if (task.getParentId() != null) {
                TaskEntity parent = taskDAO.read(task.getParentId());
                checkOwner(parent);
            }
            task.setUserId(user.getId());
            TaskEntity taskEntity = taskDAO.create(mapper.map(task, TaskEntity.class));
            return mapper.map(taskEntity, TaskDTO.class);
        });
    }

    public TaskDTO createFromTemplate(Integer templateId) throws PersistException {
        return daoUtil.callOnContext(session -> {
            DAOFactory daoFactory = DAOProvider.getDAOFactory();
            TemplateDAO templateDAO = daoFactory.getDao(session, TemplateDAO.class);
            TaskDAO taskDAO = daoFactory.getDao(session, TaskDAO.class);
            TemplateEntity template = templateDAO.read(templateId);
            if (template == null) {
                throw new PersistException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, templateId));
            }
            TaskEntity task = createTaskFromTemplate(template, mapper.map(user, UserEntity.class));
            taskDAO.create(task);
            return mapper.map(task, TaskDTO.class);
        });
    }

    private TaskEntity createTaskFromTemplate(TemplateEntity template, UserEntity user) {
        TaskEntity task = new TaskEntity();
        task.setUser(user);
        task.setOrigin(template);
        task.setName(template.getName());
        task.setDescription(template.getDescription());
        task.setOrder(template.getOrder());
        template.getChildren().forEach(childTemplate ->
                task.getChildren().add(createTaskFromTemplate(childTemplate, user)));
        return task;
    }

    public void delete(Integer taskId) throws PersistException {
        daoUtil.execute(TaskDAO.class, taskDAO -> {
            TaskEntity task = taskDAO.read(taskId);
            checkOwner(task);
            taskDAO.delete(taskId);
        });
    }

    public void update(TaskDTO task) throws PersistException {
        daoUtil.execute(TaskDAO.class, taskDAO ->
                taskDAO.update(mapper.map(task, TaskEntity.class)));
    }

    private void checkOwner(TaskEntity task) {
        if (!Objects.equals(task.getUser().getId(), user.getId())) {
            throw new SecurityException(ACCESS_ERROR);
        }
    }
}
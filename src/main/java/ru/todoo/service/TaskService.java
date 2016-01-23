package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.TaskDAO;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.dto.Task;
import ru.todoo.domain.dto.User;
import ru.todoo.domain.entity.TaskEntity;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskService {
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";

    @Resource
    private TaskDAO taskDAO;

    @Resource
    private TemplateDAO templateDAO;

    @Resource(name = "authorizedUser")
    private User authorizedUser;

    @Autowired
    private Mapper mapper;

    @Transactional(readOnly = true)
    public List<Task> readAllRoot() {
        return mapToTasksWithoutChildren(taskDAO.readRootByUser(authorizedUser.getId()));
    }

    private List<Task> mapToTasksWithoutChildren(List<TaskEntity> taskEntities) {
        return taskEntities.stream().
                map(taskEntity -> mapper.map(taskEntity, Task.class, "taskWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Task read(Integer id) {
        TaskEntity taskEntity = taskDAO.read(id);
        Task task = mapper.map(taskEntity, Task.class);
        return task;
    }

    @Transactional
    public Task create(Task task) {
        task.setUserId(authorizedUser.getId());
        TaskEntity taskEntity = mapper.map(task, TaskEntity.class);
        taskEntity = taskDAO.create(taskEntity);
        task = mapper.map(taskEntity, Task.class);
        return task;
    }

    @Transactional
    public Task createFromTemplate(Integer id) {
        TemplateEntity templateEntity = templateDAO.read(id);
        if (templateEntity == null) {
            throw new PersistenceException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, id));
        }
        UserEntity userEntity = mapper.map(authorizedUser, UserEntity.class);
        TaskEntity taskEntity = createTaskHierarchyFromTemplate(templateEntity, userEntity);
        taskEntity = taskDAO.create(taskEntity);
        Task task = mapper.map(taskEntity, Task.class);
        return task;
    }

    private TaskEntity createTaskHierarchyFromTemplate(TemplateEntity templateEntity, UserEntity userEntity) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUser(userEntity);
        taskEntity.setOrigin(templateEntity);
        taskEntity.setName(templateEntity.getName());
        taskEntity.setDescription(templateEntity.getDescription());
        taskEntity.setOrder(templateEntity.getOrder());
        templateEntity.getChildren().stream().filter(Objects::nonNull).forEach(childTemplateEntity ->
                taskEntity.getChildren().add(createTaskHierarchyFromTemplate(childTemplateEntity, userEntity)));
        return taskEntity;
    }

    @Transactional
    public void delete(Integer id) {
        taskDAO.delete(id);
    }

    @Transactional
    public void update(Task task) {
        TaskEntity entity = mapper.map(task, TaskEntity.class);
        taskDAO.update(entity);
    }
}
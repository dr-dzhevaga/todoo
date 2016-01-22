package ru.todoo.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.TaskEntity;

import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 22.01.2016.
 */
@Aspect
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskServiceAccessControlAspect {
    private static final String ACCESS_ERROR = "Access denied";

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private UserDTO userDTO;

    @Before("(" +
            "execution(* ru.todoo.service.TaskService.read(..)) || " +
            "execution(* ru.todoo.service.TaskService.delete(..))" +
            ") && args(id))")
    public void checkAccess(Integer id) {
        checkOwnerIsAuthorizedUser(id);
    }

    @Before("(" +
            "execution(* ru.todoo.service.TaskService.create(..)) || " +
            "execution(* ru.todoo.service.TaskService.update(..))" +
            ") && args(task))")
    public void checkAccess(TaskDTO task) {
        checkOwnerIsAuthorizedUser(task.getId());
        checkOwnerIsAuthorizedUser(task.getParentId());
    }

    private void checkOwnerIsAuthorizedUser(Integer id) {
        if (id != null) {
            TaskEntity entity = taskDAO.read(id);
            if (!Objects.equals(entity.getUser().getId(), userDTO.getId())) {
                throw new SecurityException(ACCESS_ERROR);
            }
        }
    }
}

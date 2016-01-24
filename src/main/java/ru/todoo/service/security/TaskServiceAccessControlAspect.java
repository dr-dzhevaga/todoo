package ru.todoo.service.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.dto.Task;
import ru.todoo.domain.dto.User;
import ru.todoo.domain.entity.TaskEntity;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 22.01.2016.
 */
@Aspect
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskServiceAccessControlAspect {
    private static final String ACCESS_ERROR = "Access is denied";

    @Resource
    private TaskDAO taskDAO;

    @Resource(name = "authorizedUser")
    private User authorizedUser;

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
    public void checkAccess(Task task) {
        checkOwnerIsAuthorizedUser(task.getId());
        checkOwnerIsAuthorizedUser(task.getParentId());
    }

    private void checkOwnerIsAuthorizedUser(Integer id) {
        if (id != null) {
            TaskEntity entity = taskDAO.read(id);
            if (!Objects.equals(entity.getUser().getId(), authorizedUser.getId())) {
                throw new SecurityException(ACCESS_ERROR);
            }
        }
    }
}

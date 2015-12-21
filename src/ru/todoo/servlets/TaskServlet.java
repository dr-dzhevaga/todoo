package ru.todoo.servlets;

import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.service.ServiceProvider;
import ru.todoo.service.TaskService;
import ru.todoo.utils.JsonUtil;
import ru.todoo.utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 03.12.2015.
 */
@WebServlet("/api/tasks/*")
@ServletSecurity(@HttpConstraint(rolesAllowed = "user"))
public class TaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String filter = Objects.toString(request.getParameter("filter"), "");
            String id = request.getParameter("id");
            String username = request.getRemoteUser();
            List<TaskDTO> tasks;
            TaskService taskService = ServiceProvider.getTaskService(username);
            switch (filter) {
                case "parent":
                    tasks = taskService.read(Integer.valueOf(id)).getChildren();
                    break;
                default:
                    tasks = taskService.readAll();
                    break;
            }
            return JsonUtil.toJsonArray(tasks);
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String templateId = request.getParameter("templateId");
        String username = request.getRemoteUser();
        ServletUtil.process(response, () -> {
            TaskDTO task;
            TaskService taskService = ServiceProvider.getTaskService(username);
            if (templateId == null) {
                String json = ServletUtil.readContent(request);
                task = JsonUtil.toObject(json, TaskDTO.class);
                task = taskService.create(task);
            } else {
                task = taskService.createFromTemplate(Integer.valueOf(templateId));
            }
            return JsonUtil.toJsonObject(task);
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getRemoteUser();
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            ServiceProvider.getTaskService(username).delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Task is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getRemoteUser();
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            TaskDTO task = JsonUtil.toObject(json, TaskDTO.class);
            ServiceProvider.getTaskService(username).update(task);
            return JsonUtil.getBuilder().addProperty("message", "Task is updated").build();
        });
    }
}

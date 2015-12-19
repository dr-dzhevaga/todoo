package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Task;
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
            JsonArray taskArray;
            TaskService taskService = ServiceProvider.getTaskService(username);
            switch (filter) {
                case "parent":
                    Task task = taskService.read(Integer.valueOf(id));
                    taskArray = JsonUtil.toJsonArray(task);
                    break;
                default:
                    List<Task> tasks = taskService.readAll();
                    taskArray = JsonUtil.toJsonArray(tasks);
                    break;
            }
            return JsonUtil.getBuilder().add("data", taskArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String templateId = request.getParameter("templateId");
        String username = request.getRemoteUser();
        ServletUtil.process(response, () -> {
            Task task;
            TaskService taskService = ServiceProvider.getTaskService(username);
            if (templateId == null) {
                String json = ServletUtil.readContent(request);
                task = JsonUtil.toObject(json, Task.class);
                task = taskService.create(task);
            } else {
                task = taskService.createFromTemplate(Integer.valueOf(templateId));
            }
            JsonObject taskObject = JsonUtil.toJsonObject(task);
            return JsonUtil.getBuilder().add("data", taskObject).build();
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
            Task task = JsonUtil.toObject(json, Task.class);
            ServiceProvider.getTaskService(username).update(task);
            return JsonUtil.getBuilder().addProperty("message", "Task is updated").build();
        });
    }
}

package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Task;
import ru.todoo.service.ServiceProvider;
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
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String filter = Objects.toString(request.getParameter("filter"), "");
            String id = request.getParameter("id");
            List<Task> taskList;
            JsonArray taskArray;
            switch (filter) {
                case "parent":
                    taskList = serviceProvider.getTaskService().readHierarchy(Integer.valueOf(id));
                    taskArray = JsonUtil.toJsonArray(taskList, Integer.valueOf(id));
                    break;
                default:
                    taskList = serviceProvider.getTaskService().readByUser(ServletUtil.getUser().getId());
                    taskArray = JsonUtil.toJsonArray(taskList);
                    break;
            }
            return JsonUtil.getBuilder().add("data", taskArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String templateId = request.getParameter("templateId");
        Integer userId = ServletUtil.getUser().getId();
        ServletUtil.process(response, () -> {
            Task task;
            if (templateId == null) {
                String json = ServletUtil.readContent(request);
                task = JsonUtil.toObject(json, Task.class);
                task.setUserId(userId);
                task = serviceProvider.getTaskService().create(task);
            } else {
                task = serviceProvider.getTaskService().createFromTemplate(Integer.valueOf(templateId), userId);
            }
            JsonObject taskObject = JsonUtil.toJsonObject(task);
            return JsonUtil.getBuilder().add("data", taskObject).build();
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            serviceProvider.getTaskService().delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Task is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Task task = JsonUtil.toObject(json, Task.class);
            serviceProvider.getTaskService().update(task);
            return JsonUtil.getBuilder().addProperty("message", "Task is updated").build();
        });
    }
}

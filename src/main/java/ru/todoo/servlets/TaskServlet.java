package ru.todoo.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.todoo.domain.dto.TaskDTO;
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
    @Autowired
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String filter = Objects.toString(request.getParameter("filter"), "");
            String id = request.getParameter("id");
            List<TaskDTO> tasks;
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
        ServletUtil.process(response, () -> {
            TaskDTO task;
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
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            taskService.delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Task is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            TaskDTO task = JsonUtil.toObject(json, TaskDTO.class);
            taskService.update(task);
            return JsonUtil.getBuilder().addProperty("message", "Task is updated").build();
        });
    }
}

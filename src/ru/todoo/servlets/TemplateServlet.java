package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Task;
import ru.todoo.service.ServiceProvider;
import ru.todoo.service.TemplateService;
import ru.todoo.utils.JsonUtil;
import ru.todoo.utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@WebServlet("/api/templates/*")
@ServletSecurity(
        httpMethodConstraints = {
                @HttpMethodConstraint(value = "POST", rolesAllowed = "admin"),
                @HttpMethodConstraint(value = "PUT", rolesAllowed = "admin"),
                @HttpMethodConstraint(value = "DELETE", rolesAllowed = "admin")
        }
)
public class TemplateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String filter = Objects.toString(request.getParameter("filter"), "all");
            String id = request.getParameter("id");
            List<Task> templatesList;
            JsonArray templatesArray;
            TemplateService templateService = ServiceProvider.getTemplateService();
            switch (filter) {
                case "parent":
                    templatesList = templateService.readHierarchy(Integer.valueOf(id));
                    templatesArray = JsonUtil.toJsonArray(templatesList, Integer.valueOf(id));
                    break;
                case "category":
                    templatesList = templateService.readByCategory(Integer.valueOf(id));
                    templatesArray = JsonUtil.toJsonArray(templatesList);
                    break;
                case "popular":
                    templatesList = templateService.readPopular();
                    templatesArray = JsonUtil.toJsonArray(templatesList);
                    break;
                default:
                    templatesList = templateService.readAll();
                    templatesArray = JsonUtil.toJsonArray(templatesList);
                    break;
            }
            return JsonUtil.getBuilder().add("data", templatesArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Task template = JsonUtil.toObject(json, Task.class);
            template.setUserId(ServletUtil.getUser(request).getId());
            template = ServiceProvider.getTemplateService().create(template);
            JsonObject templateObject = JsonUtil.toJsonObject(template);
            return JsonUtil.getBuilder().add("data", templateObject).build();
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            ServiceProvider.getTemplateService().delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Template is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Task task = JsonUtil.toObject(json, Task.class);
            ServiceProvider.getTemplateService().update(task);
            return JsonUtil.getBuilder().addProperty("message", "Template is updated").build();
        });
    }
}

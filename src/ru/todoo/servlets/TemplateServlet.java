package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Task;
import ru.todoo.service.ServiceProvider;
import ru.todoo.utils.JsonUtil;
import ru.todoo.utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@WebServlet("/api/templates/*")
public class TemplateServlet extends HttpServlet {
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletUtil.process(response, () -> {
            String filter = request.getParameter("filter");
            String id = request.getParameter("id");
            List<Task> templates;
            if ("category".equals(filter)) {
                templates = serviceProvider.getTemplateService().readByCategory(Integer.valueOf(id));
            } else if ("popular".equals(filter)) {
                templates = serviceProvider.getTemplateService().readPopular();
            } else {
                templates = serviceProvider.getTemplateService().readAll();
            }
            JsonArray templatesArray = JsonUtil.toJsonArray(templates);
            return JsonUtil.getBuilder().add("data", templatesArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Task template = JsonUtil.toObject(json, Task.class);
            template.setUserId(ServletUtil.getUser().getId());
            template = serviceProvider.getTemplateService().create(template);
            JsonObject templateObject = JsonUtil.toJsonObject(template);
            return JsonUtil.getBuilder().add("data", templateObject).build();
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            serviceProvider.getTemplateService().delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Template is deleted").build();
        });
    }
}

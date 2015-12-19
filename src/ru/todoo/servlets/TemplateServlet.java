package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Template;
import ru.todoo.domain.User;
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
            JsonArray templatesArray;
            TemplateService templateService = ServiceProvider.getTemplateService();
            switch (filter) {
                case "parent": {
                    Template template = templateService.read(Integer.valueOf(id));
                    templatesArray = JsonUtil.toJsonArray(template);
                    break;
                }
                case "category": {
                    List<Template> templates = templateService.readByCategory(Integer.valueOf(id));
                    templatesArray = JsonUtil.toJsonArray(templates);
                    break;
                }
                case "popular": {
                    List<Template> templates = templateService.readPopular();
                    templatesArray = JsonUtil.toJsonArray(templates);
                    break;
                }
                default: {
                    List<Template> templates = templateService.readAll();
                    templatesArray = JsonUtil.toJsonArray(templates);
                    break;
                }
            }
            return JsonUtil.getBuilder().add("data", templatesArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            User user = ServletUtil.getUser(request);
            String json = ServletUtil.readContent(request);
            JsonObject parameters = JsonUtil.toJsonObject(json);
            TemplateService templateService = ServiceProvider.getTemplateService();
            if (parameters.has("sourceType") && parameters.get("sourceType").getAsString().equals("text")) {
                String text = parameters.get("text").getAsString();
                Integer templateId = parameters.get("templateId").getAsInt();
                Template result = templateService.createStepsFromText(text, templateId, user);
                return JsonUtil.getBuilder().add("data", JsonUtil.toJsonArray(result)).build();
            } else {
                Template template = JsonUtil.toObject(json, Template.class);
                template.setUser(user);
                template = templateService.create(template);
                return JsonUtil.getBuilder().add("data", JsonUtil.toJsonObject(template)).build();
            }
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
            Template template = JsonUtil.toObject(json, Template.class);
            ServiceProvider.getTemplateService().update(template);
            return JsonUtil.getBuilder().addProperty("message", "Template is updated").build();
        });
    }
}

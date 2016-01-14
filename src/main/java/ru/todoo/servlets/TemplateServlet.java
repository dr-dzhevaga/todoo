package ru.todoo.servlets;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.todoo.domain.dto.TemplateDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.service.TemplateService;
import ru.todoo.service.UserService;
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
    @Autowired
    private TemplateService templateService;

    @Autowired
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String filter = Objects.toString(request.getParameter("filter"), "all");
            String id = request.getParameter("id");
            List<TemplateDTO> templates;
            switch (filter) {
                case "parent":
                    templates = templateService.read(Integer.valueOf(id)).getChildren();
                    break;
                case "category":
                    templates = templateService.readByCategory(Integer.valueOf(id));
                    break;
                case "popular":
                    templates = templateService.readPopular();
                    break;
                default:
                    templates = templateService.readAll();
                    break;
            }
            return JsonUtil.toJsonArray(templates);
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            UserDTO user = userService.readByLogin(request.getRemoteUser());
            String json = ServletUtil.readContent(request);
            JsonObject parameters = JsonUtil.toJsonObject(json);
            TemplateDTO template;
            if (parameters.has("sourceType") && parameters.get("sourceType").getAsString().equals("text")) {
                String text = parameters.get("text").getAsString();
                Integer templateId = parameters.get("templateId").getAsInt();
                template = templateService.createStepsFromText(text, templateId, user.getId());
            } else {
                template = JsonUtil.toObject(json, TemplateDTO.class);
                template.setUserId(user.getId());
                template = templateService.create(template);
            }
            return JsonUtil.toJsonObject(template);
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            templateService.delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Template is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            TemplateDTO template = JsonUtil.toObject(json, TemplateDTO.class);
            templateService.update(template);
            return JsonUtil.getBuilder().addProperty("message", "Template is updated").build();
        });
    }
}

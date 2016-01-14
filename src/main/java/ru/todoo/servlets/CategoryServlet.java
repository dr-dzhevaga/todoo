package ru.todoo.servlets;

import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.todoo.domain.dto.CategoryDTO;
import ru.todoo.service.CategoryService;
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

/**
 * Created by Dmitriy Dzhevaga on 23.11.2015.
 */
@WebServlet("/api/categories/*")
@ServletSecurity(
        httpMethodConstraints = {
                @HttpMethodConstraint(value = "POST", rolesAllowed = "admin"),
                @HttpMethodConstraint(value = "PUT", rolesAllowed = "admin"),
                @HttpMethodConstraint(value = "DELETE", rolesAllowed = "admin")
        }
)
public class CategoryServlet extends HttpServlet {
    @Autowired
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            JsonArray categoriesArray = new JsonArray();
            categoriesArray.add(JsonUtil.getBuilder().
                    addProperty("id", 1).
                    addProperty("name", "All").
                    addProperty("filter", "all").
                    build()
            );
            categoriesArray.add(JsonUtil.getBuilder().
                    addProperty("id", 2).
                    addProperty("name", "Popular").
                    addProperty("filter", "popular").
                    build()
            );
            List<CategoryDTO> categoriesList = categoryService.readAll();
            categoriesList.forEach(category -> categoriesArray.add(JsonUtil.getBuilder(category).
                    addProperty("filter", "category").
                    build())
            );
            return categoriesArray;
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            CategoryDTO category = JsonUtil.toObject(json, CategoryDTO.class);
            category = categoryService.create(category);
            return JsonUtil.getBuilder(category).addProperty("filter", "category").build();
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            categoryService.delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Category is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            CategoryDTO category = JsonUtil.toObject(json, CategoryDTO.class);
            categoryService.update(category);
            return JsonUtil.getBuilder().addProperty("message", "Category is updated").build();
        });
    }
}

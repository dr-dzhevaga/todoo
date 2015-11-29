package ru.todoo.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.domain.Category;
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
 * Created by Dmitriy Dzhevaga on 23.11.2015.
 */
@WebServlet("/api/categories/*")
public class CategoryServlet extends HttpServlet {
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletUtil.process(response, () -> {
            JsonArray categoriesArray = new JsonArray();
            categoriesArray.add(JsonUtil.getBuilder().
                    addProperty("id", 0).
                    addProperty("name", "All").
                    addProperty("filter", "all").
                    build()
            );
            categoriesArray.add(JsonUtil.getBuilder().
                    addProperty("id", 1).
                    addProperty("name", "Popular").
                    addProperty("filter", "popular").
                    build()
            );
            List<Category> categoriesList = serviceProvider.getCategoryService().readAll();
            categoriesList.forEach(category -> categoriesArray.add(JsonUtil.getBuilder(category).
                    addProperty("filter", "category").
                    build())
            );
            return JsonUtil.getBuilder().add("data", categoriesArray).build();
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Category category = JsonUtil.toObject(json, Category.class);
            category = serviceProvider.getCategoryService().create(category);
            JsonObject categoryObject = JsonUtil.getBuilder(category).addProperty("filter", "category").build();
            return JsonUtil.getBuilder().add("data", categoryObject).build();
        });
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            int id = ServletUtil.getIdFromUri(request);
            serviceProvider.getCategoryService().delete(id);
            return JsonUtil.getBuilder().addProperty("message", "Category is deleted").build();
        });
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.process(response, () -> {
            String json = ServletUtil.readContent(request);
            Category category = JsonUtil.toObject(json, Category.class);
            serviceProvider.getCategoryService().update(category);
            return JsonUtil.getBuilder().addProperty("message", "Category is updated").build();
        });
    }
}

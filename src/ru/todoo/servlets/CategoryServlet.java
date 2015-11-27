package ru.todoo.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.Category;
import ru.todoo.service.ServiceProvider;

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
@WebServlet("/category/*")
public class CategoryServlet extends HttpServlet {
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        JsonObject result = new JsonObject();
        try {
            JsonArray categoriesArray = new JsonArray();
            {
                JsonObject categoryObject = new JsonObject();
                categoryObject.addProperty("id", 0);
                categoryObject.addProperty("name", "All");
                categoryObject.addProperty("filter", "all");
                categoriesArray.add(categoryObject);
            }
            {
                JsonObject categoryObject = new JsonObject();
                categoryObject.addProperty("id", 1);
                categoryObject.addProperty("name", "Popular");
                categoryObject.addProperty("filter", "popular");
                categoriesArray.add(categoryObject);
            }
            List<Category> categoriesList = serviceProvider.getCategoryService().getAllCategories();
            new Gson().toJsonTree(categoriesList).getAsJsonArray().forEach(category -> {
                category.getAsJsonObject().addProperty("filter", "category");
                categoriesArray.add(category);
            });
            result.add("data", categoriesArray);
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        JsonObject result = new JsonObject();
        try {
            String name = req.getParameter("name");
            Category category = serviceProvider.getCategoryService().addCategory(name);
            JsonObject categoryObject = new Gson().toJsonTree(category).getAsJsonObject();
            categoryObject.addProperty("filter", "category");
            result.add("data", categoryObject);
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        try {
            String id = req.getPathInfo().replaceAll("/", "");
            serviceProvider.getCategoryService().deleteCategory(Integer.valueOf(id));
            result.addProperty("message", "Category is deleted");
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        try {
            String json = req.getReader().lines().reduce("", (s1, s2) -> s1 + s2);
            Category category = new Gson().fromJson(json, Category.class);
            serviceProvider.getCategoryService().updateCategory(category);
            result.addProperty("message", "Category is updated");
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }
}
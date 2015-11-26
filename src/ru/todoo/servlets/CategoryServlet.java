package ru.todoo.servlets;

import com.google.gson.Gson;
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
@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private static final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        JsonObject result = new JsonObject();
        try {
            List<Category> categories = serviceProvider.getCategoryService().getAllCategories();
            result.add("data", new Gson().toJsonTree(categories));
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        resp.setContentType("application/json;charset=utf-8");
        JsonObject result = new JsonObject();
        try {
            Category category = serviceProvider.getCategoryService().addCategory(name);
            result.add("data", new Gson().toJsonTree(category));
        } catch (PersistException e) {
            result.addProperty("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.getWriter().print(result.toString());
    }
}
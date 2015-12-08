package ru.todoo.servlets;

import ru.todoo.dao.PersistException;
import ru.todoo.domain.User;
import ru.todoo.service.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dmitriy Dzhevaga on 07.12.2015.
 */
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            ServiceProvider.getUserService().create(user);
            request.login(login, password);
            response.sendRedirect("/");
        } catch (PersistException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/registration.jsp").forward(request, response);
        }
    }
}

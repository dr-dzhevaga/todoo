package ru.todoo.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.service.UserService;

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
    @Autowired
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            UserDTO user = new UserDTO();
            user.setLogin(login);
            user.setPassword(password);
            userService.create(user);
            request.login(login, password);
            response.sendRedirect("/");
        } catch (PersistException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/registration.jsp").forward(request, response);
        }
    }
}

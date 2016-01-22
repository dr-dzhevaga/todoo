package ru.todoo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.security.SecurityUtils;
import ru.todoo.service.UserService;

import javax.persistence.PersistenceException;

/**
 * Created by Dmitriy Dzhevaga on 07.12.2015.
 */
@Controller
@RequestMapping(value = "/registration")
public class Registration {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "registration";
    }

    @RequestMapping(method = RequestMethod.POST)
    public View create(UserDTO user, RedirectAttributes redirectAttributes) {
        try {
            userService.create(user);
            SecurityUtils.authorizeUser(user);
            return new RedirectView("/");
        } catch (PersistenceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/registration");
        }
    }
}
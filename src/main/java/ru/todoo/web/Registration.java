package ru.todoo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.todoo.domain.dto.User;
import ru.todoo.service.security.UserService;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

/**
 * Created by Dmitriy Dzhevaga on 07.12.2015.
 */
@Controller
@RequestMapping(value = "/registration")
public class Registration {
    @Resource
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "registration";
    }

    @RequestMapping(method = RequestMethod.POST)
    public View create(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.create(user);
            user = userService.loadUserByUsername(user.getUsername());
            userService.authorizeUser(user);
            return new RedirectView("/");
        } catch (PersistenceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/registration");
        }
    }
}
package ru.todoo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.todoo.domain.dto.UserDTO;
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
    public String create(UserDTO user, RedirectAttributes redirectAttributes) {
        try {
            userService.create(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        } catch (PersistenceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registration";
        }
    }
}
package ru.todoo.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.todoo.domain.dto.TemplateDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.service.TemplateService;
import ru.todoo.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@RestController
@RequestMapping("/api/templates")
public class Template {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TemplateDTO> readAll() {
        return templateService.readAll();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=parent")
    public List<TemplateDTO> readByParent(@RequestParam Integer id) {
        return templateService.read(id).getChildren();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=category")
    public List<TemplateDTO> readByCategory(@RequestParam Integer id) {
        return templateService.readByCategory(id);
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=popular")
    public List<TemplateDTO> readPopular() {
        return templateService.readPopular();
    }

    @RequestMapping(method = RequestMethod.POST)
    public TemplateDTO create(TemplateDTO template, Principal principal) {
        UserDTO user = userService.readByLogin(principal.getName());
        template.setUserId(user.getId());
        return templateService.create(template);
    }

    @RequestMapping(method = RequestMethod.POST, params = "sourceType=text")
    public TemplateDTO create(@RequestParam Integer templateId, @RequestParam String text, Principal principal) {
        UserDTO user = userService.readByLogin(principal.getName());
        return templateService.createStepsFromText(text, templateId, user.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String delete(@PathVariable("id") Integer id) {
        templateService.delete(id);
        return "{\"message\" : \"Template is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(TemplateDTO template) {
        templateService.update(template);
        return "{\"message\" : \"Template is updated\"}";
    }
}

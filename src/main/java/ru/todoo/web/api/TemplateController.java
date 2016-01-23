package ru.todoo.web.api;

import org.springframework.web.bind.annotation.*;
import ru.todoo.domain.dto.Template;
import ru.todoo.service.TemplateService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@RestController
@RequestMapping("/api/templates")
public class TemplateController {
    @Resource
    private TemplateService templateService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ru.todoo.domain.dto.Template> readAll() {
        return templateService.readAllRoot();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=parent")
    public List<ru.todoo.domain.dto.Template> readByParent(@RequestParam Integer id) {
        return templateService.read(id).getChildren();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=category")
    public List<ru.todoo.domain.dto.Template> readByCategory(@RequestParam Integer id) {
        return templateService.readRootByCategory(id);
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=popular")
    public List<ru.todoo.domain.dto.Template> readPopular() {
        return templateService.readPopularRoot();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ru.todoo.domain.dto.Template create(@RequestBody Template template) {
        return templateService.create(template);
    }

    @RequestMapping(method = RequestMethod.POST, params = "sourceType=text")
    public ru.todoo.domain.dto.Template create(@RequestParam Integer templateId, @RequestParam String text) {
        return templateService.createStepsFromText(text, templateId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String delete(@PathVariable("id") Integer id) {
        templateService.delete(id);
        return "{\"message\" : \"Template is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestBody ru.todoo.domain.dto.Template template) {
        templateService.update(template);
        return "{\"message\" : \"Template is updated\"}";
    }
}

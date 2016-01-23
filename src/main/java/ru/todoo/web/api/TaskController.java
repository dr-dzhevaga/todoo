package ru.todoo.web.api;

import org.springframework.web.bind.annotation.*;
import ru.todoo.service.TaskService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 03.12.2015.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Resource
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ru.todoo.domain.dto.Task> readAll() {
        return taskService.readAllRoot();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=parent")
    public List<ru.todoo.domain.dto.Task> read(@RequestParam Integer id) {
        return taskService.read(id).getChildren();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ru.todoo.domain.dto.Task create(@RequestBody ru.todoo.domain.dto.Task task) {
        return taskService.create(task);
    }

    @RequestMapping(method = RequestMethod.POST, params = "templateId")
    public ru.todoo.domain.dto.Task createFromTemplate(@RequestParam Integer templateId) {
        return taskService.createFromTemplate(templateId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String delete(@PathVariable("id") Integer id) {
        taskService.delete(id);
        return "{\"message\" : \"Task is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestBody ru.todoo.domain.dto.Task task) {
        taskService.update(task);
        return "{\"message\" : \"Task is updated\"}";
    }
}

package ru.todoo.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.service.TaskService;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 03.12.2015.
 */
@RestController
@RequestMapping("/api/tasks")
public class Task {
    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TaskDTO> readAll() {
        return taskService.readAll();
    }

    @RequestMapping(method = RequestMethod.GET, params = "filter=parent")
    public List<TaskDTO> read(@RequestParam Integer id) {
        return taskService.read(id).getChildren();
    }

    @RequestMapping(method = RequestMethod.POST)
    public TaskDTO create(TaskDTO task) {
        return taskService.create(task);
    }

    @RequestMapping(method = RequestMethod.POST, params = "templateId")
    public TaskDTO createFromTemplate(@RequestParam Integer templateId) {
        return taskService.createFromTemplate(templateId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String delete(@PathVariable("id") Integer id) {
        taskService.delete(id);
        return "{\"message\" : \"Task is deleted\"}";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(TaskDTO task) {
        taskService.update(task);
        return "{\"message\" : \"Task is updated\"}";
    }

    @ExceptionHandler
    public String handleException(Exception e) {
        return "{\"message\" : \"" + e.getLocalizedMessage() + "\"}";
    }
}

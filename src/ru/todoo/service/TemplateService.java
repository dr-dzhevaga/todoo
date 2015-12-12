package ru.todoo.service;

import ru.todoo.dao.PersistException;
import ru.todoo.dao.TaskDAO;
import ru.todoo.domain.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    public List<Task> readAll() throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, TaskDAO::readAllTaskTemplates);
    }

    public List<Task> readByCategory(Integer categoryId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> taskDAO.readTaskTemplatesByCategory(categoryId));
    }

    public List<Task> readPopular() throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, TaskDAO::readPopularTaskTemplates);
    }

    public List<Task> readHierarchy(Integer parentId) throws PersistException {
        return DAOHelper.callOnDAO(TaskDAO.class, false, taskDAO -> taskDAO.readStructure(parentId));
    }

    public Task create(Task task) throws PersistException {
        task.setTemplate(true);
        return DAOHelper.callOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.create(task));
    }

    public List<Task> createFromText(String text, Integer parentId, Integer userId) throws PersistException {
        String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
        List<Task> result = new ArrayList<>();
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> {
            Integer secondLevelParentId = parentId;
            for (String step : steps) {
                boolean isFirstLevelChild = !Character.isWhitespace(step.charAt(0));
                String[] items = step.trim().split("&&");
                String name = items[0];
                String description = items.length > 1 ? items[1] : "";
                Task template = new Task();
                template.setUserId(userId);
                template.setTemplate(true);
                template.setParentId(isFirstLevelChild ? parentId : secondLevelParentId);
                template.setName(name);
                template.setDescription(description);
                template = taskDAO.create(template);
                result.add(template);
                if (isFirstLevelChild) {
                    secondLevelParentId = template.getId();
                }
            }
        });
        return result;
    }

    public void delete(Integer taskId) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.deleteStructure(taskId));
    }

    public void update(Task task) throws PersistException {
        DAOHelper.executeOnDAO(TaskDAO.class, true, taskDAO -> taskDAO.update(task));
    }
}

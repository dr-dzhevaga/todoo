package ru.todoo.service;

import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.dao.generic.GenericDAO;
import ru.todoo.domain.Template;
import ru.todoo.domain.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();

    public List<Template> readAll() throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, GenericDAO::readAll);
    }

    public List<Template> readByCategory(Integer categoryId) throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, templateDAO -> templateDAO.readByCategory(categoryId));
    }

    public List<Template> readPopular() throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, TemplateDAO::readPopular);
    }

    public Template read(Integer id) throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, templateDAO -> templateDAO.read(id));
    }

    public Template create(Template template) throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, templateDAO -> templateDAO.create(template));
    }

    public Template createStepsFromText(String text, Integer rootId, User user) throws PersistException {
        return daoUtil.callOnDAO(TemplateDAO.class, taskDAO -> {
            String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
            Template root = taskDAO.read(rootId);
            Template node = root;
            for (String step : steps) {
                boolean isLeaf = Character.isWhitespace(step.charAt(0));
                String[] items = step.trim().split("&&");
                String name = items[0];
                String description = items.length > 1 ? items[1] : "";
                Template template = new Template();
                template.setUser(user);
                template.setName(name);
                template.setDescription(description);
                if (!isLeaf) {
                    root.getChildren().add(template);
                    node = template;
                } else {
                    node.getChildren().add(template);
                }
            }
            taskDAO.update(root);
            return root;
        });
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.executeOnDAO(TemplateDAO.class, templateDAO -> templateDAO.delete(id));
    }

    public void update(Template template) throws PersistException {
        daoUtil.executeOnDAO(TemplateDAO.class, templateDAO -> templateDAO.update(template));
    }
}

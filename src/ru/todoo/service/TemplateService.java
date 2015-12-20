package ru.todoo.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.*;
import ru.todoo.domain.dto.TemplateDTO;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    public List<TemplateDTO> readAll() throws PersistException {
        return daoUtil.call(TemplateDAO.class, templateDAO -> {
            List<TemplateEntity> templates = templateDAO.readAllRoot();
            return templates.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public List<TemplateDTO> readByCategory(Integer categoryId) throws PersistException {
        return daoUtil.call(TemplateDAO.class, templateDAO -> {
            List<TemplateEntity> templates = templateDAO.readRootByCategory(categoryId);
            return templates.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public List<TemplateDTO> readPopular() throws PersistException {
        return daoUtil.call(TemplateDAO.class, templateDAO -> {
            List<TemplateEntity> templates = templateDAO.readPopularRoot();
            return templates.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public TemplateDTO read(Integer id) throws PersistException {
        return daoUtil.call(TemplateDAO.class, templateDAO -> {
            TemplateEntity template = templateDAO.read(id);
            return mapper.map(template, TemplateDTO.class);
        });
    }

    public TemplateDTO create(TemplateDTO template) throws PersistException {
        return daoUtil.call(TemplateDAO.class, templateDAO -> {
            TemplateEntity templateEntity = templateDAO.create(mapper.map(template, TemplateEntity.class));
            return mapper.map(templateEntity, TemplateDTO.class);
        });
    }

    public TemplateEntity createStepsFromText(String text, Integer rootId, Integer userId) throws PersistException {
        return daoUtil.callOnContext(session -> {
            String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
            UserDAO userDAO = DAOProvider.getDAOFactory().getDao(session, UserDAO.class);
            UserEntity user = userDAO.read(userId);
            TemplateDAO templateDAO = DAOProvider.getDAOFactory().getDao(session, TemplateDAO.class);
            TemplateEntity root = templateDAO.read(rootId);
            TemplateEntity node = root;
            for (String step : steps) {
                boolean isLeaf = Character.isWhitespace(step.charAt(0));
                String[] items = step.trim().split("&&");
                String name = items[0];
                String description = items.length > 1 ? items[1] : "";
                TemplateEntity template = new TemplateEntity();
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
            templateDAO.update(root);
            return root;
        });
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.execute(TemplateDAO.class, templateDAO -> templateDAO.delete(id));
    }

    public void update(TemplateDTO template) throws PersistException {
        daoUtil.execute(TemplateDAO.class, templateDAO ->
                templateDAO.update(mapper.map(template, TemplateEntity.class)));
    }
}

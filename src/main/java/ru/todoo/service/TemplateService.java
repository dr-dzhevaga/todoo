package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.dto.TemplateDTO;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@Service
public class TemplateService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    private TemplateDAO templateDAO;
    @Autowired
    private Mapper mapper;

    @Transactional(readOnly = true)
    public List<TemplateDTO> readAll() {
        List<TemplateEntity> entities = templateDAO.readAllRoot();
        return entities.stream().
                map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TemplateDTO> readByCategory(Integer categoryId) {
        List<TemplateEntity> entities = templateDAO.readRootByCategory(categoryId);
        return entities.stream().
                map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TemplateDTO> readPopular() {
        List<TemplateEntity> entities = templateDAO.readPopularRoot();
        return entities.stream().
                map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TemplateDTO read(Integer id) {
        TemplateEntity entity = templateDAO.read(id);
        return mapper.map(entity, TemplateDTO.class);
    }

    @Transactional
    public TemplateDTO create(TemplateDTO dto) {
        TemplateEntity entity = mapper.map(dto, TemplateEntity.class);
        return mapper.map(templateDAO.create(entity), TemplateDTO.class);
    }

    @Transactional
    public TemplateDTO createStepsFromText(String text, Integer templateId, Integer userId) {
        String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
        UserEntity user = userDAO.read(userId);
        TemplateEntity rootTemplateEntity = templateDAO.read(templateId);
        TemplateEntity nodeTemplateEntity = rootTemplateEntity;
        for (String step : steps) {
            boolean isNode = !Character.isWhitespace(step.charAt(0));
            String[] items = step.trim().split("&&");
            String name = items[0];
            String description = items.length > 1 ? items[1] : "";
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setUser(user);
            templateEntity.setName(name);
            templateEntity.setDescription(description);
            if (isNode) {
                rootTemplateEntity.getChildren().add(templateEntity);
                nodeTemplateEntity = templateEntity;
            } else {
                nodeTemplateEntity.getChildren().add(templateEntity);
            }
        }
        templateDAO.create(rootTemplateEntity);
        return mapper.map(rootTemplateEntity, TemplateDTO.class);
    }

    @Transactional
    public void delete(Integer id) {
        templateDAO.delete(id);
    }

    @Transactional
    public void update(TemplateDTO dto) {
        TemplateEntity entity = mapper.map(dto, TemplateEntity.class);
        templateDAO.update(entity);
    }
}
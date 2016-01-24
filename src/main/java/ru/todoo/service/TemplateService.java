package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.dto.Template;
import ru.todoo.domain.dto.User;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;
import ru.todoo.service.security.UserService;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
@Service
public class TemplateService {
    @Resource
    private TemplateDAO templateDAO;

    @Resource
    private UserService userService;

    @Resource
    private Mapper mapper;

    @Transactional(readOnly = true)
    public List<Template> readAllRoot() {
        return mapToTemplatesWithoutChildren(templateDAO.readAllRoot());
    }

    @Transactional(readOnly = true)
    public List<Template> readRootByCategory(Integer categoryId) {
        return mapToTemplatesWithoutChildren(templateDAO.readRootByCategory(categoryId));
    }

    @Transactional(readOnly = true)
    public List<Template> readPopularRoot() {
        return mapToTemplatesWithoutChildren(templateDAO.readPopularRoot());
    }

    private List<Template> mapToTemplatesWithoutChildren(List<TemplateEntity> templateEntities) {
        return templateEntities.stream().
                map(templateEntity -> mapper.map(templateEntity, Template.class, "templateWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Template read(Integer id) {
        TemplateEntity templateEntity = templateDAO.read(id);
        Template template = mapper.map(templateEntity, Template.class);
        return template;
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Template create(Template template) {
        User user = userService.getAuthorizedUser();
        template.setUserId(user.getId());
        TemplateEntity templateEntity = mapper.map(template, TemplateEntity.class);
        templateEntity = templateDAO.create(templateEntity);
        template = mapper.map(templateEntity, Template.class);
        return template;
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Template createStepsFromText(String text, Integer templateId) {
        User user = userService.getAuthorizedUser();
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
        TemplateEntity rootTemplateEntity = templateDAO.read(templateId);
        TemplateEntity nodeTemplateEntity = rootTemplateEntity;
        for (String step : steps) {
            boolean isNode = !Character.isWhitespace(step.charAt(0));
            String[] items = step.trim().split("&&");
            String name = items[0];
            String description = items.length > 1 ? items[1] : "";
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setUser(userEntity);
            templateEntity.setName(name);
            templateEntity.setDescription(description);
            if (isNode) {
                rootTemplateEntity.getChildren().add(templateEntity);
                nodeTemplateEntity = templateEntity;
            } else {
                nodeTemplateEntity.getChildren().add(templateEntity);
            }
        }
        rootTemplateEntity = templateDAO.create(rootTemplateEntity);
        Template template = mapper.map(rootTemplateEntity, Template.class);
        return template;
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public void delete(Integer id) {
        templateDAO.delete(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public void update(Template template) {
        TemplateEntity entity = mapper.map(template, TemplateEntity.class);
        templateDAO.update(entity);
    }
}
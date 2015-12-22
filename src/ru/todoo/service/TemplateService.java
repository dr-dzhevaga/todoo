package ru.todoo.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.*;
import ru.todoo.domain.dto.TemplateDTO;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 29.11.2015.
 */
public class TemplateService {
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    public List<TemplateDTO> readAll() throws PersistException {
        return daoUtil.call(TemplateDAO.class, dao -> {
            List<TemplateEntity> entities = dao.readAllRoot();
            return entities.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public List<TemplateDTO> readByCategory(Integer categoryId) throws PersistException {
        return daoUtil.call(TemplateDAO.class, dao -> {
            List<TemplateEntity> entities = dao.readRootByCategory(categoryId);
            return entities.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public List<TemplateDTO> readPopular() throws PersistException {
        return daoUtil.call(TemplateDAO.class, dao -> {
            List<TemplateEntity> entities = dao.readPopularRoot();
            return entities.stream().
                    map(template -> mapper.map(template, TemplateDTO.class, "templateWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public TemplateDTO read(Integer id) throws PersistException {
        return daoUtil.call(TemplateDAO.class, dao -> {
            TemplateEntity entity = dao.read(id);
            return mapper.map(entity, TemplateDTO.class);
        });
    }

    public TemplateDTO create(TemplateDTO dto) throws PersistException {
        return daoUtil.call(TemplateDAO.class, dao -> {
            TemplateEntity entity = mapper.map(dto, TemplateEntity.class);
            if (dto.getParentId() == null) {
                dao.create(entity);
            } else {
                TemplateEntity parentEntity = dao.read(dto.getParentId());
                parentEntity.getChildren().add(entity);
                dao.update(parentEntity);
            }
            return mapper.map(entity, TemplateDTO.class);
        });
    }

    public TemplateDTO createStepsFromText(String text, Integer rootId, Integer userId) throws PersistException {
        return daoUtil.callOnContext(session -> {
            String[] steps = Arrays.stream(text.split("\\r?\\n")).filter(line -> !line.isEmpty()).toArray(String[]::new);
            UserDAO userDAO = DAOProvider.getDAOFactory().getDao(session, UserDAO.class);
            TemplateDAO templateDAO = DAOProvider.getDAOFactory().getDao(session, TemplateDAO.class);
            UserEntity user = userDAO.read(userId);
            TemplateEntity rootTemplateEntity = templateDAO.read(rootId);
            TemplateEntity nodeTemplateEntity = rootTemplateEntity;
            for (String step : steps) {
                boolean isLeaf = Character.isWhitespace(step.charAt(0));
                String[] items = step.trim().split("&&");
                String name = items[0];
                String description = items.length > 1 ? items[1] : "";
                TemplateEntity templateEntity = new TemplateEntity();
                templateEntity.setUser(user);
                templateEntity.setName(name);
                templateEntity.setDescription(description);
                if (!isLeaf) {
                    rootTemplateEntity.getChildren().add(templateEntity);
                    nodeTemplateEntity = templateEntity;
                } else {
                    nodeTemplateEntity.getChildren().add(templateEntity);
                }
            }
            templateDAO.update(rootTemplateEntity);
            return mapper.map(rootTemplateEntity, TemplateDTO.class);
        });
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.execute(TemplateDAO.class, dao -> {
            dao.delete(id);
        });
    }

    public void update(TemplateDTO dto) throws PersistException {
        daoUtil.execute(TemplateDAO.class, dao -> {
            TemplateEntity entity = dao.read(dto.getId());
            if (entity.getParent() == null) {
                mapper.map(dto, entity);
                dao.update(entity);
            } else {
                TemplateEntity parentEntity = entity.getParent();
                if (!Objects.equals(parentEntity.getId(), dto.getParentId())) {
                    TemplateEntity newParentEntity = dao.read(dto.getParentId());
                    newParentEntity.getChildren().add(entity);
                    dao.update(newParentEntity);
                } else if (!Objects.equals(entity.getOrder(), dto.getOrder())) {
                    parentEntity.getChildren().remove(entity);
                    parentEntity.getChildren().add(dto.getOrder(), entity);
                    dao.update(parentEntity);
                } else {
                    mapper.map(dto, entity);
                    dao.update(entity);
                }
            }
        });
    }
}
package ru.todoo.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.*;
import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.TaskEntity;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class TaskService {
    private static final String ACCESS_ERROR = "Access denied";
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
    private final UserDTO userDTO;

    public TaskService(String username) throws PersistException {
        userDTO = ServiceProvider.getUserService().readByLogin(username);
    }

    public List<TaskDTO> readAll() throws PersistException {
        return daoUtil.call(TaskDAO.class, dao -> {
            List<TaskEntity> entities = dao.readRootByUser(userDTO.getId());
            return entities.stream().
                    map(task -> mapper.map(task, TaskDTO.class, "taskWithoutChildren")).
                    collect(Collectors.toList());
        });
    }

    public TaskDTO read(Integer id) throws PersistException {
        return daoUtil.call(TaskDAO.class, dao -> {
            checkOwner(dao, id);
            TaskEntity entity = dao.read(id);
            return mapper.map(entity, TaskDTO.class);
        });
    }

    public TaskDTO create(TaskDTO dto) throws PersistException {
        return daoUtil.call(TaskDAO.class, dao -> {
            if (dto.getParentId() != null) {
                checkOwner(dao, dto.getParentId());
            }
            dto.setUserId(userDTO.getId());
            TaskEntity entity = dao.create(mapper.map(dto, TaskEntity.class));
            return mapper.map(entity, TaskDTO.class);
        });
    }

    public TaskDTO createFromTemplate(Integer id) throws PersistException {
        return daoUtil.callOnContext(session -> {
            DAOFactory daoFactory = DAOProvider.getDAOFactory();
            TemplateDAO templateDAO = daoFactory.getDao(session, TemplateDAO.class);
            TaskDAO taskDAO = daoFactory.getDao(session, TaskDAO.class);
            TemplateEntity templateEntity = templateDAO.read(id);
            if (templateEntity == null) {
                throw new PersistException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, id));
            }
            TaskEntity taskEntity = createFromTemplate(templateEntity, mapper.map(userDTO, UserEntity.class));
            taskDAO.create(taskEntity);
            return mapper.map(taskEntity, TaskDTO.class);
        });
    }

    private TaskEntity createFromTemplate(TemplateEntity templateEntity, UserEntity userEntity) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUser(userEntity);
        taskEntity.setOrigin(templateEntity);
        taskEntity.setName(templateEntity.getName());
        taskEntity.setDescription(templateEntity.getDescription());
        taskEntity.setOrder(templateEntity.getOrder());
        templateEntity.getChildren().stream().filter(Objects::nonNull).forEach(childTemplateEntity ->
                taskEntity.getChildren().add(createFromTemplate(childTemplateEntity, userEntity)));
        return taskEntity;
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.execute(TaskDAO.class, dao -> {
            checkOwner(dao, id);
            dao.delete(id);
        });
    }

    public void update(TaskDTO dto) throws PersistException {
        daoUtil.execute(TaskDAO.class, dao -> {
            TaskEntity entity = mapper.map(dto, TaskEntity.class);
            dao.update(entity);
        });
    }

    private void checkOwner(TaskDAO dao, Integer id) throws PersistException {
        TaskEntity entity = dao.read(id);
        if (!Objects.equals(entity.getUser().getId(), userDTO.getId())) {
            throw new SecurityException(ACCESS_ERROR);
        }
    }
}
package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.TaskDAO;
import ru.todoo.dao.TemplateDAO;
import ru.todoo.domain.dto.TaskDTO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.TaskEntity;
import ru.todoo.domain.entity.TemplateEntity;
import ru.todoo.domain.entity.UserEntity;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
public class TaskService {
    private static final String ACCESS_ERROR = "Access denied";
    private static final String TEMPLATE_S_IS_NOT_FOUND_ERROR = "Template %s is not found";

    private final UserDTO userDTO;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private TemplateDAO templateDAO;

    // TODO: move all conversions to controller? Use json annotations for mapping? Apply Open Session in View pattern?
    @Autowired
    private Mapper mapper;

    @Autowired
    public TaskService(UserService userService) throws PersistenceException {
        // TODO: add user resolving from context
        userDTO = userService.loadUserByUsername("admin");
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> readAll() throws PersistenceException {
        List<TaskEntity> entities = taskDAO.readRootByUser(userDTO.getId());
        return entities.stream().
                map(task -> mapper.map(task, TaskDTO.class, "taskWithoutChildren")).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDTO read(Integer id) throws PersistenceException {
        checkOwner(taskDAO, id);
        TaskEntity entity = taskDAO.read(id);
        return mapper.map(entity, TaskDTO.class);
    }

    @Transactional
    public TaskDTO create(TaskDTO dto) throws PersistenceException {
        if (dto.getParentId() != null) {
            checkOwner(taskDAO, dto.getParentId());
        }
        dto.setUserId(userDTO.getId());
        TaskEntity entity = taskDAO.create(mapper.map(dto, TaskEntity.class));
        return mapper.map(entity, TaskDTO.class);
    }

    @Transactional
    public TaskDTO createFromTemplate(Integer id) throws PersistenceException {
        TemplateEntity templateEntity = templateDAO.read(id);
        if (templateEntity == null) {
            throw new PersistenceException(String.format(TEMPLATE_S_IS_NOT_FOUND_ERROR, id));
        }
        TaskEntity taskEntity = createFromTemplate(templateEntity, mapper.map(userDTO, UserEntity.class));
        taskDAO.create(taskEntity);
        return mapper.map(taskEntity, TaskDTO.class);
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

    @Transactional
    public void delete(Integer id) throws PersistenceException {
        checkOwner(taskDAO, id);
        taskDAO.delete(id);
    }

    @Transactional
    public void update(TaskDTO dto) throws PersistenceException {
        TaskEntity entity = mapper.map(dto, TaskEntity.class);
        taskDAO.update(entity);
    }

    // TODO: apply AOP?
    private void checkOwner(TaskDAO dao, Integer id) throws PersistenceException {
        TaskEntity entity = dao.read(id);
        if (!Objects.equals(entity.getUser().getId(), userDTO.getId())) {
            throw new SecurityException(ACCESS_ERROR);
        }
    }
}
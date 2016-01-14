package ru.todoo.service;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.dto.CategoryDTO;
import ru.todoo.domain.entity.CategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private Mapper mapper;

    @Transactional
    public CategoryDTO create(CategoryDTO dto) throws PersistException {
        CategoryEntity entity = categoryDAO.create(mapper.map(dto, CategoryEntity.class));
        return mapper.map(entity, CategoryDTO.class);
    }

    @Transactional(readOnly = true)
    public CategoryDTO read(Integer id) throws PersistException {
        CategoryEntity entity = categoryDAO.read(id);
        return mapper.map(entity, CategoryDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> readAll() throws PersistException {
        List<CategoryEntity> entities = categoryDAO.readAll();
        return entities.stream().
                map(categoryEntity -> mapper.map(categoryEntity, CategoryDTO.class)).
                collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) throws PersistException {
        categoryDAO.delete(id);
    }

    @Transactional
    public void update(CategoryDTO dto) throws PersistException {
        categoryDAO.update(mapper.map(dto, CategoryEntity.class));
    }
}

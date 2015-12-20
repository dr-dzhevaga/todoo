package ru.todoo.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import ru.todoo.dao.CategoryDAO;
import ru.todoo.dao.DAOProvider;
import ru.todoo.dao.DAOUtil;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.dto.CategoryDTO;
import ru.todoo.domain.entity.CategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
public class CategoryService {
    private static final DAOUtil daoUtil = DAOProvider.getDAOUtil();
    private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    public CategoryDTO create(CategoryDTO category) throws PersistException {
        return daoUtil.call(CategoryDAO.class, categoryDAO -> {
            CategoryEntity categoryEntity = categoryDAO.create(mapper.map(category, CategoryEntity.class));
            return mapper.map(categoryEntity, CategoryDTO.class);
        });
    }

    public CategoryDTO read(Integer id) throws PersistException {
        return daoUtil.call(CategoryDAO.class, categoryDAO -> {
            CategoryEntity categoryEntity = categoryDAO.read(id);
            return mapper.map(categoryEntity, CategoryDTO.class);
        });
    }

    public List<CategoryDTO> readAll() throws PersistException {
        return daoUtil.call(CategoryDAO.class, categoryDAO -> {
            List<CategoryEntity> categoryEntities = categoryDAO.readAll();
            return categoryEntities.stream().
                    map(categoryEntity -> mapper.map(categoryEntity, CategoryDTO.class)).
                    collect(Collectors.toList());
        });
    }

    public void delete(Integer id) throws PersistException {
        daoUtil.execute(CategoryDAO.class, categoryDAO -> categoryDAO.delete(id));
    }

    public void update(CategoryDTO category) throws PersistException {
        daoUtil.execute(CategoryDAO.class, categoryDAO ->
                categoryDAO.update(mapper.map(category, CategoryEntity.class)));
    }
}

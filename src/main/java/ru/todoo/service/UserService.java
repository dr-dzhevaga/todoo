package ru.todoo.service;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.dto.UserDTO;
import ru.todoo.domain.entity.UserEntity;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "user";

    @Autowired
    UserDAO userDAO;

    @Autowired
    private Mapper mapper;

    @Transactional(readOnly = true)
    public UserDTO readByUsername(String username) {
        UserEntity entity = userDAO.readByUsername(username);
        return mapper.map(entity, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDAO.readByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new User(username, user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<String> roles) {
        return roles.stream().
                map(role -> "ROLE_" + role.toUpperCase()).
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
    }

    @Transactional
    public void create(UserDTO dto) {
        if (StringUtils.isBlank(dto.getUsername()) || StringUtils.isBlank(dto.getPassword())) {
            throw new PersistenceException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(dto.getUsername())) {
            throw new PersistenceException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_USER_ROLE);
        UserEntity entity = mapper.map(dto, UserEntity.class);
        entity.setRoles(roles);
        userDAO.create(entity);
    }

    private boolean isLoginUnique(String login) {
        UserEntity entity = userDAO.readByUsername(login);
        return entity == null;
    }

    @Transactional
    public void delete(Integer id) {
        userDAO.delete(id);
    }
}
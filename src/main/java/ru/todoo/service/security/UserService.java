package ru.todoo.service.security;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.todoo.dao.UserDAO;
import ru.todoo.domain.dto.User;
import ru.todoo.domain.entity.UserEntity;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 06.11.2015.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final String USERNAME_OR_PASSWORD_IS_EMPTY_ERROR = "Username or password is empty";
    private static final String USERNAME_IS_NOT_UNIQUE_ERROR = "Username is not unique";
    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Resource
    private UserDAO userDAO;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDAO.readByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = mapper.map(userEntity, User.class);
        return user;
    }

    @Transactional
    public void create(User user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            throw new PersistenceException(USERNAME_OR_PASSWORD_IS_EMPTY_ERROR);
        }
        if (!isLoginUnique(user.getUsername())) {
            throw new PersistenceException(USERNAME_IS_NOT_UNIQUE_ERROR);
        }
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_USER_ROLE);
        userEntity.setRoles(roles);
        userDAO.create(userEntity);
        mapper.map(userEntity, user);
    }

    private boolean isLoginUnique(String login) {
        UserEntity userEntity = userDAO.readByUsername(login);
        return userEntity == null;
    }

    public User getAuthorizedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void authorizeUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
package ru.todoo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.todoo.domain.dto.UserDTO;

/**
 * Created by Dmitriy Dzhevaga on 22.01.2016.
 */
public class SecurityUtils {
    private SecurityUtils() {
    }

    public static UserDTO getAuthorizedUser() {
        return (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void authorizeUser(UserDTO user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

package ru.todoo.domain.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class UserDTO {
    private Integer id;
    private String login;
    private String password;
    private Set<String> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

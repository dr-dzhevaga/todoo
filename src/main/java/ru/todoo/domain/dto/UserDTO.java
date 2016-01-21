package ru.todoo.domain.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class UserDTO {
    private Integer id;
    private String username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

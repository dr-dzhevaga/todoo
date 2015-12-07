package ru.todoo.domain;

/**
 * Created by Dmitriy Dzhevaga on 07.12.2015.
 */
public class Role {
    private String login;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

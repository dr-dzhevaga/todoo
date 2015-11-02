package ru.todoo.domain;

import ru.todoo.dao.generic.Identified;

import java.sql.Timestamp;

/**
 * Created by Dmitriy Dzhevaga on 28.10.2015.
 */
public class User implements Identified<Integer> {
    private Integer id;
    private String login;
    private String password;
    private Timestamp created;
    private Timestamp modified;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreated() {
        return created;
    }

    protected void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}

package ru.todoo.domain.entity;

import org.hibernate.annotations.UpdateTimestamp;
import ru.todoo.dao.generic.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@Table(name = "USERS", schema = "APP", catalog = "")
public class UserEntity implements Serializable, Identifiable<Integer> {
    private Integer id;
    private String login;
    private String password;
    private Timestamp created;
    private Timestamp modified;
    private Set<String> roles = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, updatable = false)
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CREATED", insertable = false, updatable = false)
    public Timestamp getCreated() {
        return created;
    }

    private void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @UpdateTimestamp
    @Column(name = "MODIFIED")
    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @PreUpdate
    private void updateModified() {
        this.modified = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Basic
    @Column(name = "LOGIN")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection
    @CollectionTable(name = "ROLES", joinColumns = @JoinColumn(name = "LOGIN", referencedColumnName = "LOGIN"))
    @Column(name = "ROLE")
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (modified != null ? !modified.equals(that.modified) : that.modified != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return !(password != null ? !password.equals(that.password) : that.password != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}

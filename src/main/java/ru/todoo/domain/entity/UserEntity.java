package ru.todoo.domain.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@Table(name = "USERS", schema = "APP", catalog = "")
public class UserEntity extends IdentifiableEntity<Integer> {
    @Basic
    @Column(name = "LOGIN")
    private String username;

    @Basic
    @Column(name = "PASSWORD")
    private String password;

    @Basic
    @Column(name = "CREATED", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private Timestamp created;

    @Basic
    @UpdateTimestamp
    @Column(name = "MODIFIED")
    private Timestamp modified;

    @ElementCollection
    @CollectionTable(name = "ROLES", joinColumns = @JoinColumn(name = "LOGIN", referencedColumnName = "LOGIN"))
    @Column(name = "ROLE")
    private Set<String> roles = new HashSet<>();

    public Timestamp getCreated() {
        return created;
    }

    private void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    private void updateModified() {
        this.modified = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return !(password != null ? !password.equals(that.password) : that.password != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}

package ru.todoo.domain.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Dmitriy Dzhevaga on 17.12.2015.
 */
@Entity
@Table(name = "TASKS", schema = "APP")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "IS_TEMPLATE", discriminatorType = DiscriminatorType.INTEGER)
public abstract class BaseTaskEntity<T extends BaseTaskEntity<T, PK>, PK extends Serializable> extends HierarchicalEntity<T, PK> {
    @Basic
    @Column(name = "NAME")
    protected String name;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private UserEntity user;

    @Basic
    @Column(name = "CREATED", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private Timestamp created;

    @Basic
    @UpdateTimestamp
    @Column(name = "MODIFIED")
    private Timestamp modified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
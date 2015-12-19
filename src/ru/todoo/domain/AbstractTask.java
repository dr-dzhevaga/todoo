package ru.todoo.domain;

import ru.todoo.dao.generic.Identifiable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Dmitriy Dzhevaga on 17.12.2015.
 */
@Entity
@Table(name = "TASKS", schema = "APP")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "IS_TEMPLATE", discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractTask implements Identifiable<Integer> {
    private Integer id;
    private Boolean isTemplate;
    private Integer order;
    private String name;
    private String description;
    private User user;
    private Timestamp created;
    private Timestamp modified;

    @Id
    @GeneratedValue
    @Column(name = "ID", insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ORDER_NUMBER")
    @OrderColumn
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "IS_TEMPLATE", updatable = false)
    private Boolean getTemplate() {
        return isTemplate;
    }

    private void setTemplate(Boolean template) {
        isTemplate = template;
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
    @Column(name = "MODIFIED", updatable = false)
    public Timestamp getModified() {
        return modified;
    }

    private void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @PreUpdate
    private void updateModified() {
        this.modified = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask that = (AbstractTask) o;
        if (id != that.id) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (isTemplate != null ? !isTemplate.equals(that.isTemplate) : that.isTemplate != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        return !(modified != null ? !modified.equals(that.modified) : that.modified != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isTemplate != null ? isTemplate.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }
}

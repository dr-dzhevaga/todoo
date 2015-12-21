package ru.todoo.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "1")
public class TemplateEntity extends AbstractTaskEntity {
    private CategoryEntity category;
    private List<TemplateEntity> children = new ArrayList<>();
    private TemplateEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "ORDER_NUMBER")
    public List<TemplateEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TemplateEntity> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public TemplateEntity getParent() {
        return parent;
    }

    public void setParent(TemplateEntity parent) {
        this.parent = parent;
    }
}

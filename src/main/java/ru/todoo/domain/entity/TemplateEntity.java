package ru.todoo.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "1")
public class TemplateEntity extends BaseTaskEntity<TemplateEntity, Integer> {
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "ORDER_NUMBER")
    private List<TemplateEntity> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateEntity parent;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @Override
    public List<TemplateEntity> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TemplateEntity> children) {
        this.children = children;
    }

    @Override
    public TemplateEntity getParent() {
        return parent;
    }

    @Override
    public void setParent(TemplateEntity parent) {
        this.parent = parent;
    }
}
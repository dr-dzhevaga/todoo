package ru.todoo.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "1")
public class Template extends AbstractTask {
    private Category category;
    private List<Template> children = new ArrayList<>();
    private Template parent;

    @ManyToOne(fetch = FetchType.LAZY)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARENT_ID")
    public List<Template> getChildren() {
        return children;
    }

    private void setChildren(List<Template> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Template getParent() {
        return parent;
    }

    public void setParent(Template parent) {
        this.parent = parent;
    }
}

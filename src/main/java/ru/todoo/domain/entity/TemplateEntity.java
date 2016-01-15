package ru.todoo.domain.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by Dmitriy Dzhevaga on 19.12.2015.
 */
@Entity
@DiscriminatorValue(value = "1")
public class TemplateEntity extends BaseTaskEntity<TemplateEntity, Integer> {
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
package ru.todoo.domain.dto;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 20.12.2015.
 */
public class TemplateDTO extends BaseTaskDTO {
    private Integer categoryId;
    private List<TemplateDTO> children;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<TemplateDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TemplateDTO> children) {
        this.children = children;
    }
}

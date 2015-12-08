package ru.todoo.dao.generic;

import java.io.Serializable;

/**
 * Created by Dmitriy Dzhevaga on 03.11.2015.
 */
public interface Structured<PK extends Serializable> {
    PK getParentId();

    void setParentId(PK parentId);

    Integer getOrder();

    void setOrder(Integer order);
}

package ru.todoo.dao.generic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dzhevaga Dmitriy on 22.12.2015.
 */
public interface Hierarchical<PK extends Serializable> extends Identifiable<PK> {
    Hierarchical getParent();

    List<? extends Hierarchical> getChildren();

    Integer getOrder();
}

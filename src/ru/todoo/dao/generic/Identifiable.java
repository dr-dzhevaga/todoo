package ru.todoo.dao.generic;

import java.io.Serializable;

public interface Identifiable<PK extends Serializable> {
    PK getId();
}

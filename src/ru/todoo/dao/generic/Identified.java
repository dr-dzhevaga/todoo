package ru.todoo.dao.generic;

import java.io.Serializable;

public interface Identified<PK extends Serializable> {
    PK getId();
}

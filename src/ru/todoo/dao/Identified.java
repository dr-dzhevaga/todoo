package ru.todoo.dao;

import java.io.Serializable;

public interface Identified<K extends Serializable> {
    public K getId();
}

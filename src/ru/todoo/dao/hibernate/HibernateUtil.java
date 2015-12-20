package ru.todoo.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Dmitriy Dzhevaga on 17.12.2015.
 */
public class HibernateUtil {
    private static volatile SessionFactory INSTANCE;

    public static SessionFactory getSessionFactory() {
        if (INSTANCE == null) {
            synchronized (SessionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Configuration().configure().buildSessionFactory();
                }
            }
        }
        return INSTANCE;
    }
}

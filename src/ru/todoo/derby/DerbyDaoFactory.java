package ru.todoo.derby;

import ru.todoo.dao.DaoFactory;
import ru.todoo.dao.GenericDao;
import ru.todoo.dao.Identified;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.User;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class DerbyDaoFactory implements DaoFactory<Connection> {
    private final String CONNECTION_STRING = "jdbc:derby:D:/Java/todooDB";
    private final Map<Class, Function<Connection, ? extends GenericDao>> creators = new HashMap<>();

    public DerbyDaoFactory() {
        creators.put(User.class, UserDao::new);
    }

    @Override
    public Connection getContext() throws PersistException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return  connection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Identified<K>, K extends Serializable> GenericDao<T, K>
    getDao(Connection connection, Class<T> dtoClass) throws PersistException {
        Function<Connection, ? extends GenericDao> creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found");
        }
        return (GenericDao<T, K>) creator.apply(connection);
    }
}
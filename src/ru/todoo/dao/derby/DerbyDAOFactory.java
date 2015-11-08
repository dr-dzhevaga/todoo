package ru.todoo.dao.derby;

import ru.todoo.dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class DerbyDAOFactory implements DAOFactory<Connection> {
    private final static Map<Class, Function<Connection, ?>> creators = new HashMap<>();

    static {
        creators.put(UserDAO.class, DerbyUserDAO::new);
        creators.put(CategoryDAO.class, DerbyCategoryDAO::new);
        creators.put(TaskDAO.class, DerbyTaskDAO::new);
    }

    private final String CONNECTION_STRING = "jdbc:derby:D:/Java/todooDB";

    @Override
    public Connection getContext() throws PersistException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return connection;
    }

    @Override
    public <R> R getDao(Connection connection, Class<R> daoClass) throws PersistException {
        Function<Connection, ?> creator = creators.get(daoClass);
        if (creator == null) {
            throw new IllegalArgumentException("DAO for " + daoClass + " class not found");
        }
        return daoClass.cast(creator.apply(connection));
    }
}
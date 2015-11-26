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
    private static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/D:/Dmitriy/Java/db-derby-10.12.1.1-bin/db/todoo";
    private static final String DRIVER_NAME = "org.apache.derby.jdbc.ClientDriver";

    static {
        creators.put(UserDAO.class, DerbyUserDAO::new);
        creators.put(CategoryDAO.class, DerbyCategoryDAO::new);
        creators.put(TaskDAO.class, DerbyTaskDAO::new);
    }

    @Override
    public Connection getContext() throws PersistException {
        Connection connection;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException | ClassNotFoundException e) {
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
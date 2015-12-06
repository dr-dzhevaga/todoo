package ru.todoo.dao.derby;

import ru.todoo.dao.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class DerbyDAOFactory implements DAOFactory<Connection> {
    private static final String ENVIRONMENT_CONTEXT_NAME = "java:comp/env";
    private static final String DATA_SOURCE_NAME = "jdbc/todooDB";
    private final static Map<Class, Function<Connection, ?>> constructors = new HashMap<>();
    static {
        constructors.put(UserDAO.class, DerbyUserDAO::new);
        constructors.put(CategoryDAO.class, DerbyCategoryDAO::new);
        constructors.put(TaskDAO.class, DerbyTaskDAO::new);
    }

    private final DataSource dataSource;

    public DerbyDAOFactory() throws PersistException {
        try {
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup(ENVIRONMENT_CONTEXT_NAME);
            dataSource = (DataSource) environmentContext.lookup(DATA_SOURCE_NAME);
        } catch (NamingException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    @Override
    public Connection getContext() throws PersistException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    @Override
    public <R> R getDao(Connection connection, Class<R> daoClass) throws PersistException {
        Function<Connection, ?> constructor = constructors.get(daoClass);
        if (constructor == null) {
            throw new IllegalArgumentException("DAO for " + daoClass + " class not found");
        }
        return daoClass.cast(constructor.apply(connection));
    }
}
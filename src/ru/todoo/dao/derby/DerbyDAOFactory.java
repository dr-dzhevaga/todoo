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

    private final static Map<Class, Function<Connection, ?>> creators = new HashMap<>();
    static {
        creators.put(UserDAO.class, DerbyUserDAO::new);
        creators.put(CategoryDAO.class, DerbyCategoryDAO::new);
        creators.put(TaskDAO.class, DerbyTaskDAO::new);
    }

    @Override
    public Connection getContext() throws PersistException {
        try {
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup(ENVIRONMENT_CONTEXT_NAME);
            DataSource dataSource = (DataSource) environmentContext.lookup(DATA_SOURCE_NAME);
            return dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
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
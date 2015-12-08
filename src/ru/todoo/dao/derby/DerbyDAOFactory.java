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
public class DerbyDAOFactory implements DAOFactory {
    private static final String ENVIRONMENT_CONTEXT_NAME = "java:comp/env";
    private static final String DATA_SOURCE_NAME = "jdbc/todooDB";
    private static final Map<Class, Function<Connection, ?>> constructors = new HashMap<>();
    private static volatile DerbyDAOFactory INSTANCE;

    static {
        constructors.put(UserDAO.class, DerbyUserDAO::new);
        constructors.put(RoleDAO.class, DerbyRoleDAO::new);
        constructors.put(CategoryDAO.class, DerbyCategoryDAO::new);
        constructors.put(TaskDAO.class, DerbyTaskDAO::new);
    }

    private final DataSource dataSource;

    private DerbyDAOFactory() throws PersistException {
        try {
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup(ENVIRONMENT_CONTEXT_NAME);
            dataSource = (DataSource) environmentContext.lookup(DATA_SOURCE_NAME);
        } catch (NamingException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    public static DerbyDAOFactory getInstance() throws PersistException {
        if (INSTANCE == null) {
            synchronized (DAOFactoryProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DerbyDAOFactory();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Object getContext() throws PersistException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new PersistException(e, e.getMessage());
        }
    }

    @Override
    public <T> T getDao(Object context, Class<T> daoClass) throws PersistException {
        Function<Connection, ?> constructor = constructors.get(daoClass);
        if (constructor == null) {
            throw new IllegalArgumentException("DAO for " + daoClass + " class not found");
        }
        return daoClass.cast(constructor.apply((Connection) context));
    }
}
package ru.todoo.dao.derby;

import ru.todoo.dao.DAOFactory;
import ru.todoo.dao.PersistException;
import ru.todoo.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Dmitriy Dzhevaga on 01.11.2015.
 */
public class DerbyDAOFactory implements DAOFactory<Connection> {
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
    public UserDAO getUserDao(Connection context) throws PersistException {
        return new DerbyUserDAO(context);
    }
}
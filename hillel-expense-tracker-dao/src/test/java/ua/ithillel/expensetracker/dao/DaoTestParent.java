package ua.ithillel.expensetracker.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoTestParent {
    protected Connection connection;

    @BeforeEach
    public void initConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:mem:test;INIT=runscript from 'classpath:init.sql'";

        connection = DriverManager.getConnection(url);
    }

    @AfterEach
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

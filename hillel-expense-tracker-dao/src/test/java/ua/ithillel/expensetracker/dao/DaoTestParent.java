package ua.ithillel.expensetracker.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ithillel.expensetracker.config.DaoTestConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
public class DaoTestParent {
    protected Connection connection;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

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

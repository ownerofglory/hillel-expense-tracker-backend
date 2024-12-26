package ua.ithillel.expensetracker;

import ua.ithillel.expensetracker.dao.UserDao;
import ua.ithillel.expensetracker.dao.UserMySqlDao;
import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.model.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

// This class is used solely for demo purposes
// for the topic of JDBC at Hillel IT School
// you may consider not reviewing this code
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        // host:port
        // database name
        // username
        // password
        String username = System.getenv("MYSQL_USER");
        String password = System.getenv("MYSQL_PASSWORD");
        String dbname = System.getenv("MYSQL_DB_NAME");
        String url = "jdbc:mysql://127.0.0.1:3306/" + dbname;
//        String url = "jdbc:mysql://localhost:3306/" + dbname;


        Class.forName("com.mysql.cj.jdbc.Driver"); // load driver class

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            UserDao userDao = new UserMySqlDao(connection);
            List<User> all = userDao.findAll();

            Optional<User> user = userDao.find(1L);
            Optional<User> byEmail = userDao.findByEmail("john.doe@example.com");

            User user1 = new User();
            user1.setFirstname("Ivan");
            user1.setLastname("Petrenko");
            user1.setEmail("igp@example.com");
            Optional<User> save = userDao.save(user1);
            User savedUser = save.get();

            System.out.println();



        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}

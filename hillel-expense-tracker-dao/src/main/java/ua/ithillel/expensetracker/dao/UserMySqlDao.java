package ua.ithillel.expensetracker.dao;

import lombok.Data;
import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Data
public class UserMySqlDao implements UserDao {
    private final Connection connection;

    @Override
    public List<User> findAll() throws DaoException {
        try(Statement statement = connection.createStatement()) {

            boolean success = statement.execute("SELECT * FROM t_user");// DQL
            if (!success) {
                throw new DaoException("Can't find all users");
            }

            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                User newUser = new User();

                newUser.setId(resultSet.getLong("id"));
                newUser.setFirstname(resultSet.getString("first_name"));
                newUser.setLastname(resultSet.getString("last_name"));

                users.add(newUser);
            }


            return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM t_user WHERE email = ?")) {
            statement.setString(1, email);

            boolean execute = statement.execute();
            if (!execute) {
                return Optional.empty();
            }

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                User newUser = new User();
                newUser.setId(resultSet.getLong("id"));
                newUser.setFirstname(resultSet.getString("first_name"));
                newUser.setLastname(resultSet.getString("last_name"));
                newUser.setEmail(resultSet.getString("email"));
                return Optional.of(newUser);
            }


        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> find(Long id) throws DaoException {
        try(PreparedStatement statement
                    = connection.prepareStatement("SELECT " +
                " t_user.id, t_user.first_name, t_user.last_name, t_user.email," +
                " t_category.id, t_category.name" +
                " FROM t_user" +
                " JOIN t_category " +
                " ON t_user.id = t_category.user_id" +
                " WHERE t_user.id = ?")) {
            statement.setLong(1, id);

            boolean execute = statement.execute();

            if (!execute) {
                return Optional.empty();
            }

            ResultSet resultSet = statement.getResultSet();
            User newUser = null;
            while (resultSet.next()) {
                if (newUser == null) {
                    newUser = new User();
                    newUser.setId(resultSet.getLong("t_user.id"));
                    newUser.setFirstname(resultSet.getString("t_user.first_name"));
                    newUser.setLastname(resultSet.getString("t_user.last_name"));
                    newUser.setEmail(resultSet.getString("t_user.email"));

                    newUser.setCategories(new HashSet<>());
                }

                ExpenseCategory expenseCategory = new ExpenseCategory();
                expenseCategory.setId(resultSet.getLong("t_category.id"));
                expenseCategory.setName(resultSet.getString("t_category.name"));

                newUser.getCategories().add(expenseCategory);


            }

            return Optional.ofNullable(newUser);

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Optional<User> save(User user) throws DaoException {
        try (PreparedStatement statement
                     = connection.prepareStatement("INSERT INTO t_user(first_name, last_name, email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            return Optional.of(user);

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }
}

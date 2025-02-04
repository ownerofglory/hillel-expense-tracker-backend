package ua.ithillel.expensetracker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString("first_name"));
        user.setLastname(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));

        return user;
    }
}

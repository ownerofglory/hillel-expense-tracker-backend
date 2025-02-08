package ua.ithillel.expensetracker.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.mapper.UserRowMapper;
import ua.ithillel.expensetracker.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSpringJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = jdbcTemplate.query("""
                    SELECT * FROM t_user
                """, userRowMapper);

        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        User user = jdbcTemplate.queryForObject("""
                    SELECT * FROM t_user WHERE email = ?
                """, new Object[]{email},
                new int[]{Types.VARCHAR},
                userRowMapper);

        return Optional.of(user);
    }

    @Override
    public Optional<User> find(Long id) throws DaoException {
        User user = jdbcTemplate.queryForObject("""
                    SELECT * FROM t_user WHERE id = ?
                """,
                new Object[]{id},
                new int[]{Types.INTEGER},
                userRowMapper);

        return Optional.of(user);
    }

    @Override
    public Optional<User> save(User user) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO t_user(first_name, last_name, email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());

            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            throw new DaoException("Could not save user");
        }

        long savedUserId = (Integer) keyHolder.getKeyList()
                .get(0)
                .get("ID");


        user.setId(savedUserId);

        return Optional.of(user);
    }
}

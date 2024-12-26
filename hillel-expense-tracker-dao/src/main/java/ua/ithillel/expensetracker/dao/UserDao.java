package ua.ithillel.expensetracker.dao;

import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll() throws DaoException;
    Optional<User> findByEmail(String email) throws DaoException;
    Optional<User> find(Long id) throws DaoException;
    Optional<User> save(User user) throws DaoException;
}

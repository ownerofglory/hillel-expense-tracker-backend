package ua.ithillel.expensetracker.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.mapper.UserRowMapper;
import ua.ithillel.expensetracker.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserSpringJdbcDaoTest extends DaoTestParent {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserSpringJdbcDao(jdbcTemplate, new UserRowMapper());
    }

    @Test
    void findTest_userExists() throws DaoException {
        Long testId = 1L;
        Optional<User> user = userDao.find(testId);
        assertTrue(user.isPresent());
        assertTrue(user.get().getId().equals(testId));
    }

    @Test
    void findTest_userDoesNotExistReturnsNull() throws DaoException {
        Long testId = 8000000L;
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.find(testId));
    }

    @Test
    void findByEmailTest_exits() throws DaoException {
        String testEmail = "john.doe@example.com";
        Optional<User> user = userDao.findByEmail(testEmail);
        assertTrue(user.isPresent());
        assertTrue(user.get().getEmail().equals(testEmail));
    }

    @Test
    void findByEmailTest_doesNotExist() throws DaoException {
        String testEmail = "nonexisting@example.com";
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findByEmail(testEmail));
    }

    @Test
    void findAllTest_returnsNonEmptyList() throws DaoException {
        List<User> all = userDao.findAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void saveTest_returnsUserWithId() throws DaoException {
        User testUser = new User();
        testUser.setFirstname("testUser");
        testUser.setLastname("testUser");
        testUser.setEmail("testUser@example.com");

        Optional<User> saved = userDao.save(testUser);

        assertTrue(saved.isPresent());
        assertNotNull(saved.get().getId());
        assertEquals(testUser.getFirstname(), saved.get().getFirstname());
        assertEquals(testUser.getLastname(), saved.get().getLastname());
        assertEquals(testUser.getEmail(), saved.get().getEmail());
    }

    @Test
    void saveTest_ifEmailAlreadyExistsThrowsException() throws DaoException {
        User testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setEmail("john.doe@example.com");

        assertThrows(DuplicateKeyException.class, () -> userDao.save(testUser));
    }

    @Test
    void tearDown() {
        System.out.println("Test finished");
    }
}

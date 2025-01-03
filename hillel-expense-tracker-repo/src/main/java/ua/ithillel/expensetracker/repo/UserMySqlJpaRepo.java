package ua.ithillel.expensetracker.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;

import java.util.Optional;

@RequiredArgsConstructor
public class UserMySqlJpaRepo implements UserRepo {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<User> find(Long id) throws ExpenseTrackerPersistingException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            User user = entityManager.find(User.class, id);

            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) throws ExpenseTrackerPersistingException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            entityManager.persist(user);

            entityManager.getTransaction().commit();

            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> delete(User user) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }
}

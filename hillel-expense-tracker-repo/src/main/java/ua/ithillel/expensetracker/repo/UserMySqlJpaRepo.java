package ua.ithillel.expensetracker.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            TypedQuery<User> query
                    = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class); // JQL - JPA query language
            query.setParameter("email", email);
            query.setMaxResults(1);
            User userByEmail = query.getSingleResult();

            return Optional.ofNullable(userByEmail);
        }
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            entityManager.remove(user);

            entityManager.getTransaction().commit();

            return Optional.ofNullable(user);
        }
    }
}

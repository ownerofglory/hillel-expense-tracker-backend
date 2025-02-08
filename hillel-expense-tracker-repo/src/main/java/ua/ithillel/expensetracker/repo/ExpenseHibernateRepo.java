package ua.ithillel.expensetracker.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ExpenseHibernateRepo implements ExpenseRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<Expense> find(Long id) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Expense expense = session.find(Expense.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(expense);
        }
    }

    @Override
    public Optional<Expense> save(Expense expense) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            if (expense.getId() == null) {
                session.persist(expense);
            }  else {
                session.merge(expense);
            }
            session.flush();

            session.getTransaction().commit();
            return Optional.of(expense);
        }
    }

    @Override
    public List<Expense> findByUser(User user) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Expense> query
                    = session.createQuery("SELECT e FROM Expense e WHERE e.user = :user", Expense.class);// JQL, HQL
            query.setParameter("user", user);

            return query.getResultList();
        }
    }

    @Override
    public List<Expense> findByUserBetweenDates(User user, LocalDateTime from, LocalDateTime to) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Expense> query
                    = session.createQuery("""
            SELECT e FROM Expense e WHERE e.user = :user      
            AND e.createdAt BETWEEN :startDate AND :endDate                                   
            """, Expense.class);// JQL, HQL
            query.setParameter("user", user);
            query.setParameter("startDate", from);
            query.setParameter("endDate", to);

            return query.getResultList();
        }
    }

    @Override
    public List<Expense> findByCategory(ExpenseCategory category) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<Expense> query
                    = session.createQuery("SELECT e FROM Expense e WHERE e.category = :category", Expense.class);// JQL, HQL
            query.setParameter("category", category);

            return query.getResultList();
        }
    }

    @Override
    public Optional<Expense> delete(Expense expense) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.remove(expense);

            session.getTransaction().commit();

            return Optional.of(expense);
        }
    }
}

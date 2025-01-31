package ua.ithillel.expensetracker.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ExpenseCategoryHibernateRepo implements ExpenseCategoryRepo {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<ExpenseCategory> find(Long id) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            ExpenseCategory category  = session.find(ExpenseCategory.class, id);

            return Optional.ofNullable(category);
        }
    }

    @Override
    public Optional<ExpenseCategory> save(ExpenseCategory expenseCategory) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }

    @Override
    public List<ExpenseCategory> findByUser(User user) throws ExpenseTrackerPersistingException {
        try (Session session = sessionFactory.openSession()) {
            Query<ExpenseCategory> query
                    = session.createQuery("SELECT c FROM ExpenseCategory c WHERE c.user = :user", ExpenseCategory.class);// JQL, HQL
            query.setParameter("user", user);

            return query.getResultList();
        }
    }

    @Override
    public Optional<ExpenseCategory> delete(ExpenseCategory expenseCategory) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }
}

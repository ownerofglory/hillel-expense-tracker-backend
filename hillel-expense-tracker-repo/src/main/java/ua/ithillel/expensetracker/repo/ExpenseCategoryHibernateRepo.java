package ua.ithillel.expensetracker.repo;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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
        return List.of();
    }

    @Override
    public Optional<ExpenseCategory> delete(ExpenseCategory expenseCategory) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }
}

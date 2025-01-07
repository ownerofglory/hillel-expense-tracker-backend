package ua.ithillel.expensetracker;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.model.UserEntity;
import ua.ithillel.expensetracker.repo.UserMySqlJpaRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.Optional;
import java.util.Set;

/*
 Dear reviewer,
 this class is used for demo purposes as part of Java Pro course by Hillel.
 Please ignore if it doesn't comply with the project's conventions
 */
public class Application {
    public static void main(String[] args) {
        try (
                EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("expense-tracker-pu");

                EntityManager entityManager = entityManagerFactory.createEntityManager(); // works with JPA context
        ) {

            UserRepo userRepo = new UserMySqlJpaRepo(entityManagerFactory);
            Optional<User> userOpt = userRepo.find(1L);
            User user = userOpt.get();


//            User userEntity = new User();
//            userEntity.setFirstname("bbb");
//            userEntity.setLastname("bbb");
//            userEntity.setEmail("bbb@user.com");
//
//            userEntity.setCategories(Set.of(
//                    new ExpenseCategory("Food"),
//                    new ExpenseCategory("Transportation"),
//                    new ExpenseCategory("Entertainment"),
//                    new ExpenseCategory("Health")
//            ));
//
//
//            userRepo.save(userEntity);









            System.out.println();







//            UserEntity user = entityManager.find(UserEntity.class, 1L);
//
//            UserEntity userEntity = new UserEntity();
//            userEntity.setFirstName("Java");
//            userEntity.setLastName("Pro");
//            userEntity.setEmail("java@pro.com");
//
//            entityManager.getTransaction().begin(); // BEGIN
//
//            // put entity into persistence context
//            entityManager.persist(userEntity);
//
//            userEntity.setLastName("Programming");
//
//            entityManager.flush();
//
//            entityManager.getTransaction().commit(); // COMMIT;



            System.out.println();
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
        ;
    }
}

package ua.ithillel.expensetracker.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class ReportTask {
    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;

//    weekly: 0 0 * * Mon (At 12:00 AM, only on Monday)
//    monthly: 0 0 1 * * (At 12:00 AM, on day 1 of the month)
//    yearly: 0 0 0 1 1 * (At 12:00 AM, on day 1 of the month, only in January)

    @Scheduled(cron = "0 * * * * *")
    @Async
    public void doMinuteReport() {
        log.info("Report task started for a minute");
        try {

            Optional<User> user = userRepo.find(1L);
            User exisintingUser = user.get();

            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minus(1L, ChronoUnit.MINUTES);

            List<Expense> byUserBetweenDates = expenseRepo.findByUserBetweenDates(exisintingUser, startDate, endDate);

            log.info("Report task started for a minute");

        } catch (ExpenseTrackerPersistingException e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(cron = "0 0 0 * * Mon")
    @Async
    public void doWeeklyReport() {
        log.info("Report task started for a week");
    }

    @Scheduled(cron = "0 0 0 1 * * ")
    @Async
    public void doMonthlyReport() {
        log.info("Report task started for a month");
    }

    @Scheduled(cron = "0 0 0 1 1 *")
    @Async
    public void doYearlyReport() {
        log.info("Report task started for a year");
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Async
    public void doDailyReport() {
        log.info("Report task started for a day");
    }
}

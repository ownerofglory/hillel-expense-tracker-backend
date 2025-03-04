package ua.ithillel.expensetracker.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableScheduling
public class DemoTask {
    @Async
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void demoTask() {
        log.info("demoTask: " + LocalDateTime.now());
    }
}

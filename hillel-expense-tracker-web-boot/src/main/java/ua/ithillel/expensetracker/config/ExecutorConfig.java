package ua.ithillel.expensetracker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class ExecutorConfig {
    private static final int THREAD_POOL_SIZE = 1;

    @Bean(destroyMethod = "shutdown", name = "executor")
    public ExecutorService executorService() {
        log.info("Creating thread pool with {} threads", THREAD_POOL_SIZE);
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

}

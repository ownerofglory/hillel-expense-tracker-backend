package ua.ithillel.expensetracker.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.expensetracker.config.WebTestConfig;
import ua.ithillel.expensetracker.exception.GlobalExceptionHandler;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebTestConfig.class})
public class ControllerTestParent {
    protected MockMvc mockMvc;

    @Autowired
    protected GlobalExceptionHandler globalExceptionHandler;
}

package ua.ithillel.expensetracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthControllerTest extends ControllerTestParent {
    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();

        mockMvc = MockMvcBuilders.standaloneSetup(healthController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void getHealth_returnsOk() throws Exception {
        mockMvc.perform(get("/v1/health"))
                .andExpect(status().isOk());
    }
}

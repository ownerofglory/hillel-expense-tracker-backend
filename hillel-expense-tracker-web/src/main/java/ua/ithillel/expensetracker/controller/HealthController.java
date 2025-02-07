package ua.ithillel.expensetracker.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping
    public @ResponseBody
    String getHealth() {
        return "OK";
    }
}

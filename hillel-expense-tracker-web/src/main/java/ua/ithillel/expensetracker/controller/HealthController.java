package ua.ithillel.expensetracker.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String getHealth() {
        return "OK";
    }
}

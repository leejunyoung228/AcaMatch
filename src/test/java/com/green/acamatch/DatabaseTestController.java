package com.green.acamatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DatabaseTestController {

    @Autowired
    private DatabaseTestService databaseTestService;

    @GetMapping("/test-connection")
    public String testConnection() {
        return databaseTestService.testConnection();
    }
}
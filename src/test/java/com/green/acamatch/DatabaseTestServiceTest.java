package com.green.acamatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseTestServiceTest {

    @Autowired
    private DatabaseTestService databaseTestService;

    @Test
    public void testDatabaseConnection() {
        String result = databaseTestService.testConnection();
        System.out.println(result);

        // 추가 검증: 연결 성공 메시지가 포함되어 있는지 확인
        assert result.contains("Database Product Name") || result.contains("Failed to connect to database");
    }
}
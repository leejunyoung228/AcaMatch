package com.green.acamatch.excel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/acamatch"; // DB URL
    private static final String DB_USER = "root"; // 사용자 이름
    private static final String DB_PASSWORD = "green502"; // 비밀번호

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 연결에 실패했습니다.");
        }
    }
}
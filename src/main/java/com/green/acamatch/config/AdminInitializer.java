package com.green.acamatch.config;

import com.green.acamatch.entity.user.User;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "acamatch@google.com";

        // 관리자 계정이 없을 때만 생성
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setUpw(passwordEncoder.encode("Qwer@1234")); // 초기 비밀번호
            admin.setUserRole(UserRole.ADMIN);
            admin.setName("관리자");
            admin.setPhone("010-1234-5678");
            admin.setNickName("AdminUser");
            admin.setBirth(java.time.LocalDate.of(1990, 1, 1));

            userRepository.save(admin);
            System.out.println("✅ 관리자 계정 생성 완료: " + adminEmail);
        } else {
            System.out.println("ℹ️ 관리자 계정이 이미 존재합니다.");
        }
    }
}

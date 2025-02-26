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
        String adminEmail = "acamatch@gmail.com";
        String adminNickName = "AdminUser";  // 닉네임 중복 체크 추가

        // 관리자 계정이 이미 존재하면 INSERT 수행하지 않음
        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("관리자 계정이 이미 존재합니다.");
            return;
        }

        // 닉네임 중복 체크 후 중복이면 "_1" 추가
        if (userRepository.existsByNickName(adminNickName)) {
            adminNickName = adminNickName + "_1";  // 예: "AdminUser_1"
            System.out.println("닉네임 중복 감지! 변경된 닉네임: " + adminNickName);
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setUpw(passwordEncoder.encode("Qwer@1234")); // 초기 비밀번호
        admin.setUserRole(UserRole.ADMIN);
        admin.setName("관리자");
        admin.setPhone("010-1234-5678");
        admin.setNickName(adminNickName);  // 중복된 경우 변경된 닉네임 사용
        admin.setBirth(java.time.LocalDate.of(1990, 1, 1));

        userRepository.save(admin);
        System.out.println("관리자 계정 생성 완료: " + adminEmail);
    }
}

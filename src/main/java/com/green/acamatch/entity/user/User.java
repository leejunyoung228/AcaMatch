package com.green.acamatch.entity.user;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.myenum.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity //테이블을 만들고 DML때 사용
@Getter
@Setter
@DynamicUpdate
public class User extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // 'role_id' 컬럼과 'role' 객체 연결
    @Column(nullable = false)
    private UserRole userRole;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birth;

    @Unique
    @Column(length = 200, nullable = false, unique = true)
    private String nickName;

    @Unique
    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String upw;

    @Column()
    private String userPic;

    // SNS 로그인 정보 추가
    @Column(length = 20)
    private String provider; // "GOOGLE", "NAVER", "KAKAO" 등의 SNS 로그인 제공자

    @Column(length = 50)
    private String providerId; // SNS 로그인 사용자의 고유 ID

    // 일반 로그인 계정인지 확인
    public boolean isNormalLogin() {
        return provider == null;
    }

    // SNS 로그인 계정인지 확인
    public boolean isSocialLogin() {
        return provider != null;
    }
}

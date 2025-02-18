package com.green.acamatch.entity.user;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.myenum.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@DynamicUpdate
@Table(name = "user")
public class User extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}

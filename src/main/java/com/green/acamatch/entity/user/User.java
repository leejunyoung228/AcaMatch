package com.green.acamatch.entity.user;

import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private Integer signUpType;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(length = 20, nullable = false, unique = true)
    private String nickName;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String upw;

    @Column(length = 50)
    private String userPic;
}

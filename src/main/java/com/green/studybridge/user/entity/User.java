package com.green.studybridge.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // 'role_id' 컬럼과 'role' 객체 연결
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "sign_up_type", nullable = false)
    private Integer signUpType;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "nick_name", nullable = false, length = 20)
    private String nickName;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "upw", nullable = false, length = 100)
    private String upw;

    @Column(name = "user_pic", length = 50)
    private String userPic;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

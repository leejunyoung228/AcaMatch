package com.green.acamatch.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@DynamicUpdate
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

    @Column(name = "sign_up_type")
    private Integer signUpType;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "nick_name", length = 20)
    private String nickName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "upw", length = 100)
    private String upw;

    @Column(name = "user_pic", length = 50)
    private String userPic;

    @Column(name = "created_at")
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

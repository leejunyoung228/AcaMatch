package com.green.studybridge.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @JoinColumn(name = "roleId", nullable = false)
    private long roleId;

    @Column(name = "signUpType", nullable = false)
    private int signUpType;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "birth", nullable = false)
    private LocalDateTime birth;

    @Column(name = "nickName", nullable = false, length = 20)
    private String nickName;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "upw", nullable = false, length = 20)
    private String upw;

    @Column(name = "userPic", length = 50)
    private String userPic;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updatedAt")
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

package com.green.acamatch.entity.manager;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "teacher")
public class Teacher {
    @EmbeddedId
    private TeacherIds teacherIds;

    // user_id 먼저 매핑
    @ManyToOne
    @MapsId("userId") // userId를 먼저 매핑
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private AcaClass acaClass;

    @Lob
    @Column(nullable = false, length = 300)
    private String teacherComment;

    @Column(nullable = false)
    private int teacherAgree;

    @Column(nullable = false)
    private int isActive;
}
package com.green.acamatch.entity.manager;

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

    // aca_id 나중에 매핑
    @ManyToOne
    @MapsId("acaId") // acaId를 나중에 매핑
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;

    @Lob
    @Column(nullable = false, length = 300)
    private String teacherComment;

    @Column(nullable = false)
    private int teacherAgree;
}

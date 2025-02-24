package com.green.acamatch.entity.manager;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Teacher{
    @EmbeddedId
    private TeacherIds teacherIds;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String teacher_comment;

    @Column(nullable = false)
    private int teacherAgree;

}

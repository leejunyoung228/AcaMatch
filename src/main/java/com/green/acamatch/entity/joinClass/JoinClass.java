package com.green.acamatch.entity.joinClass;

import com.green.acamatch.entity.acaClass.Class;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "joinClass")
public class JoinClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinClassId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classId;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int certification;

    @Column(nullable = false)
    private LocalDate registrationDate;
}

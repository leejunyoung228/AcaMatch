package com.green.acamatch.entity.joinClass;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "join_class")
public class JoinClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinClassId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AcaClass acaClass;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 학생 또는 보호자 정보
    private User user;

    @Column(nullable = false)
    private int certification;

    @Column(nullable = false)
    private LocalDate registrationDate;
}

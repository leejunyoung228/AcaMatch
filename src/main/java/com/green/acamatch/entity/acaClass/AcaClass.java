package com.green.acamatch.entity.acaClass;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "aca_class")
public class AcaClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @ManyToOne
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;  // 학원 참조

    @Column(length = 50, nullable = false)
    private String className;

    @Column(length = 300, nullable = false)
    private String classComment;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int price;

    // 복합키 기반으로 Teacher와 연결 (TeacherIds 직접 사용)
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "teacher_user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "teacher_aca_id", referencedColumnName = "aca_id")
    })
    private Teacher teacher;
}

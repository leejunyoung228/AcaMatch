package com.green.acamatch.entity.grade;

import com.green.acamatch.entity.exam.Exam;
import com.green.acamatch.entity.joinClass.JoinClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    @ManyToOne
    @JoinColumn(name = "join_class_id", nullable = false)
    private JoinClass joinClass;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column
    private int score;

    @Column
    private int pass;

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private int processingStatus;
}

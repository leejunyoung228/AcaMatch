package com.green.acamatch.entity.exam;

import com.green.acamatch.entity.acaClass.Class;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classId;

    @Column(length = 30, nullable = false)
    private String examName;

    @Column(nullable = false)
    private int examType;
}

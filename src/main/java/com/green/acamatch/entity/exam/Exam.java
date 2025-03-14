package com.green.acamatch.entity.exam;

import com.green.acamatch.entity.acaClass.AcaClass;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@EqualsAndHashCode
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private AcaClass classId;

    @Column(length = 30, nullable = false)
    private String examName;

    @Column(nullable = false)
    private int examType;

    public Exam(Long examId, AcaClass classId, String examName, int examType) {
        this.examId = examId;
        this.classId = classId;
        this.examName = examName;
        this.examType = examType;
    }
}

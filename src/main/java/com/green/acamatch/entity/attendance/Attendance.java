package com.green.acamatch.entity.attendance;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.joinClass.JoinClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Attendance extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "join_class_id", nullable = false)
    private JoinClass joinClass;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    @Convert(converter = AttendanceStatusConverter.class) // 변환기 적용
    private AttendanceStatus status;
}
package com.green.acamatch.entity.acaClass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "weekDays")
public class WeekDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayId;

    @Column(length = 10, nullable = false)
    private String day;
}
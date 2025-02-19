package com.green.acamatch.entity.popUp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class PopUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long popUpId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 300)
    private String comment;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = 20)
    private String popUpPic;

    @Column(nullable = false)
    private int popUpShow;

    @Column(nullable = false)
    private int popUpType;
}
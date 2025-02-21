package com.green.acamatch.entity.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Dong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dongId;

    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;

    @Column(length = 10, nullable = false)
    private String dongName;
}

package com.green.acamatch.entity.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Street {
    @Id
    private Long streetId;
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @Column(length = 10, nullable = false)
    private String streetName;
}

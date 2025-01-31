package com.green.acamatch.entity.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class City {
    @Id
    private Long cityId;
    @Column(unique=true, nullable=false, length=10)
    private String cityName;
}

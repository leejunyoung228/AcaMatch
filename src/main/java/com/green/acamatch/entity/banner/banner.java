package com.green.acamatch.entity.banner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(length = 20)
    private String bannerPic;

    @Column(nullable = false)
    private int bannerType;

    @Column
    private boolean bannerShow;

    @Column(length = 20, nullable = false)
    private String acaName;

}

package com.green.acamatch.entity.banner;

import com.green.acamatch.entity.academy.Academy;
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

    @OneToOne
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;

    @Column(length = 20)
    private String bannerPic;

    @Column(nullable = false)
    private int bannerType;

    @Column
    private boolean bannerShow;

    @Column(length = 20, nullable = false)
    private String acaName;

}

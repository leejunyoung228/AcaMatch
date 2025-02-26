package com.green.acamatch.entity.banner;

import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.myenum.BannerPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerIds;

    @ManyToOne
    @JoinColumn(name = "aca_id", nullable = false)
    private PremiumAcademy premiumAcademy;

    @Column(length = 20, nullable = false)
    private String bannerPic;

    @Column
    private int bannerType = 0;

    @Column
    private int bannerShow = 1;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(length = 20, nullable = false)
    private String acaName;

    @Column
    private BannerPosition bannerPosition;

}

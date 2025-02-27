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
    // aca_id를 기본 키로 사용
    @Id
    @Column(name = "aca_id")
    private Long acaId;

    @ManyToOne
    @JoinColumn(name = "aca_id", nullable = false, insertable = false, updatable = false)
    private PremiumAcademy premiumAcademy;

    @Column
    private int bannerType = 0;

    @Column
    private LocalDate startDate = null;

    @Column
    private LocalDate endDate = null;

    @Column(length = 20, nullable = false)
    private String acaName;


}

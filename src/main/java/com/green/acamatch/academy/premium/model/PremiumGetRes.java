package com.green.acamatch.academy.premium.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PremiumGetRes {
    private Long acaId;
    private String acaName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer preCheck;
    private Integer bannerType;
    private LocalDateTime createdAt;

    // **생성자 추가 (JPA가 new 키워드로 객체를 생성할 때 필요함)**
    public PremiumGetRes(Long acaId, String acaName, LocalDate startDate, LocalDate endDate,
                         Integer preCheck, Integer bannerType, LocalDateTime createdAt) {
        this.acaId = acaId;
        this.acaName = acaName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.preCheck = preCheck;
        this.bannerType = bannerType;
        this.createdAt = createdAt;
    }

    // 기본 생성자 추가 (JPA가 리플렉션을 사용할 때 필요함)
    public PremiumGetRes() {}
}

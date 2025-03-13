package com.green.acamatch.academy.premium.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PremiumBannerExistGetRes {
    private Long acaId;
    private String acaName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int preCheck;
    private Integer bannerType;
    private LocalDateTime createdAt;
    private int countPremium;
}

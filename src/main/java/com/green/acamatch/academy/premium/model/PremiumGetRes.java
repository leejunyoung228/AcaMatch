package com.green.acamatch.academy.premium.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class PremiumGetRes {
    private Long acaId;
    private String acaName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int preCheck;
    private LocalDateTime createdAt;
}

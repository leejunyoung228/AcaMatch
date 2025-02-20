package com.green.acamatch.entity.reports;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReportsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportTypeId;

    @Column(length = 15, nullable = false, unique = true)
    private String reportTypeName; // 신고 유형 (홍보, 법령 위반 등)
}

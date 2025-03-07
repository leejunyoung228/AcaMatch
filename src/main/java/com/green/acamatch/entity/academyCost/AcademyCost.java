package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AcademyCost extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long costId;

    @Column(nullable = false)
    private int orderType;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private int cost_status;

    @Column(nullable = false)
    private String tId;

    @Column(nullable = false)
    private String partnerOrderId;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product productId;

    @Column(nullable = false)
    private double fee;

    @JoinColumn(name = "aca_id")
    @ManyToOne
    private Academy academyId;

    private LocalDateTime updatedAt;
}

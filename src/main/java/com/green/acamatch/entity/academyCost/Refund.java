package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Refund{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundId;

    @ManyToOne
    @JoinColumn(name = "cost_id", nullable = false)
    private AcademyCost academyCost;

    @Column(length = 300)
    private String refundComment;

    @Column(nullable = false)
    private int refundStatus;

    @Column(nullable = false)
    private String tid;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;

    public void setAcademyCost(long costId) {
        this.academyCost = new AcademyCost();
        this.academyCost.setCostId(costId);
    }
}

package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Refund extends UpdatedAt {
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

    public void setAcademyCost(long costId) {
        this.academyCost = new AcademyCost();
        this.academyCost.setCostId(costId);
    }
}

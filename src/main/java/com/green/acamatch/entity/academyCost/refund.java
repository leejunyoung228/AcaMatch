package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class refund extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refundId;

    @ManyToOne
    @JoinColumn(name = "cost_id", nullable = false)
    private AcademyCost academyCost;

    @Column
    private String refundComment;

    @Column(nullable = false)
    private int refundStatus;
}

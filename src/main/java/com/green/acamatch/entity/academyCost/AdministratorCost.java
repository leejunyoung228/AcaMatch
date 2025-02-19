package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AdministratorCost extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminCostId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cost_id")
    private AcademyCost academyCost;

    @ManyToOne
    @JoinColumn(name = "pre_aca_id")
    private PremiumAcademy premiumAcademy;

    @Column(nullable = false)
    private int preCost;

    @Column(nullable = false)
    private int acaCost;

    @Column(nullable = false)
    private long sumPrice;
}

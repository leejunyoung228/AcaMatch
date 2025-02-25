package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.joinClass.JoinClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AcademyCost extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long costId;

    @ManyToOne
    @JoinColumn(name = "join_class_id")
    private JoinClass joinClass;

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
    private int settlementPrice;

    @Column(nullable = false)
    private int cost_status;

    @Column(nullable = false)
    private String tId;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product productId;

    public void setProductId(Long productId) {
        this.productId = new Product();
        this.productId.setProductId(productId);
    }
}

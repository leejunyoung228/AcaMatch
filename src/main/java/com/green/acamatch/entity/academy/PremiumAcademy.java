package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class PremiumAcademy extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preAcaId;

    @ManyToOne
    @JoinColumn(name = "aca_id")
    private Academy academy;

    @Column
    private LocalDate StartDate;

    @Column
    private LocalDate endDate;

    @Column
    private int preCheck;

    @Column
    private int price;
}

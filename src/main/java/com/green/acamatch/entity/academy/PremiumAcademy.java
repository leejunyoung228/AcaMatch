package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class PremiumAcademy extends CreatedAt {
    // acaId를 PK로 사용
    @Id
    @Column(name = "aca_id")
    private Long acaId;

    @OneToOne
    @JoinColumn(name = "aca_id", insertable = false, updatable = false)
    private Academy academy;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private int preCheck;

    @Column(nullable = false)
    private int price;

    // @PreUpdate를 사용하여 preCheck가 변경되면 academy의 premium 값을 갱신하도록 처리
    @PreUpdate
    public void updateAcademyPremium() {
        if (this.preCheck == 1 && academy != null) {
            academy.setPremium(1);  // academy 테이블의 premium 컬럼을 1로 변경
        }
    }
}

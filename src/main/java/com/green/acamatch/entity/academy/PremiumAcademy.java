package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class PremiumAcademy extends CreatedAt {
    // acaId를 PK로 사용
    @Id
    private Long acaId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "aca_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Academy academy;

    @Column
    private LocalDate startDate = null;

    @Column
    private LocalDate endDate = null;

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

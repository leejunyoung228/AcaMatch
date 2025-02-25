package com.green.acamatch.entity.dailyVisitorStatus;

import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_visitor_stats", uniqueConstraints = {
        @UniqueConstraint(name = "unique_visit", columnNames = {"date", "sessionId", "ipAddress"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyVisitorStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyVisitorId;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @Column(length = 100, nullable = false)
    private String sessionId;

    @Column(length = 45, nullable = false)
    private String ipAddress;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime lastVisit;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer visitCount = 1;
}

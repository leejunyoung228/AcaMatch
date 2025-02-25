package com.green.acamatch.entity.dailyVisitorStatus;

import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @Column(length = 100, nullable = false)
    private String sessionId;

    @Column(length = 45, nullable = false)
    private String ipAddress;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastVisit;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer visitCount = 1;
}

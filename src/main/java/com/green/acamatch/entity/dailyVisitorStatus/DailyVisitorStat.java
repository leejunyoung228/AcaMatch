package com.green.acamatch.entity.dailyVisitorStatus;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_visitor_stats", uniqueConstraints = {
        @UniqueConstraint(name = "unique_visit", columnNames = {"visit_date", "session_id", "ip_address"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyVisitorStat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyVisitorId;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "session_id", length = 100, nullable = false)
    private String sessionId;

    @Column(name = "ip_address", length = 45, nullable = false)
    private String ipAddress;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime lastVisit;

    @Column(nullable = false)
    private Integer visitCount = 1;
}

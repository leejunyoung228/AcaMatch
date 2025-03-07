package com.green.acamatch.entity.reports;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Reports extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private ReportsType reportsType;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private ActionType actionType;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false) // 신고한 사용자
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id") // 신고당한 사용자
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "aca_id") // 신고당한 학원
    private Academy academy;

    @ManyToOne
    @JoinColumn(name = "review_id") // 신고당한 학원
    private Review review;

    @Column(nullable = false)
    private int processingStatus = 0; // 0: 처리 전, 1: 처리 후, 2: 무죄 판정

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime exposureEndDate;
}
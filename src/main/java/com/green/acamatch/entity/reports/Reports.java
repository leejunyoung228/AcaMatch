package com.green.acamatch.entity.reports;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reports extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "report_type_id", nullable = false)
    private ReportsType reportType; // 신고 유형 (홍보, 법령 위반 등)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 신고한 사용자

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review; // 신고된 리뷰와 연결

    @Column(nullable = false)
    private int processingStatus = 0; // 0: 대기, 1: 처리 중, 2: 완료

}
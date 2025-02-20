package com.green.acamatch.entity.review;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Review extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "requested_by", referencedColumnName = "user_id", nullable = false)
    private User requestedBy;


    @Column(nullable = false)
    private double star;

    @Column(columnDefinition = "TEXT")
    private String comment;

    // 'role_id' 컬럼과 'role' 객체 연결
    @Enumerated(EnumType.STRING) // 문자열로 저장
    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'STUDENT'")
    private UserRole roleType;

    @ManyToOne
    @JoinColumn(name = "join_class_id", nullable = false)
    private JoinClass joinClass;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int banReview = 0; // 기본값 0 (정상 상태)


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewPic> reviewPics = new ArrayList<>();

}

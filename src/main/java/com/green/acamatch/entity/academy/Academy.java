package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.location.Dong;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = false)
public class Academy extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acaId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", nullable = false, insertable=false, updatable=false)
    private Long userId;


    /*@ManyToOne
    @JoinColumn(name = "dong_id", nullable = false, insertable = false, updatable = false)
    private Dong dong;*/

    @Column(name = "dong_id", nullable = false) // Dong의 ID를 직접 저장
    private Long dongId;  // 이제 Long 타입으로 저장


    @Column(nullable = false)
    private int premium;

    @Column(length = 50, nullable = false)
    private String acaName;

    @Column(length = 15, nullable = false)
    @Pattern(regexp = "^(0(10|2|3[1-3]|4[1-3]|5[1-5]|6[1-4]))-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 맞지 않습니다.")
    private String acaPhone;

    @Column(length = 1_000)
    private String comment;

    @Column
    private int teacherNum;

    @Column(nullable = false, columnDefinition = "TIME(0)")
    private LocalTime openTime;

    @Column(nullable = false, columnDefinition = "TIME(0)")
    private LocalTime closeTime;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 50, nullable = false)
    private String detailAddress;

    @Column(length = 10, nullable = false)
    private String postNum;

    @Column(nullable = false)
    private int acaAgree = 0;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    @Column(length = 20, nullable = false)
    private String businessName;

    @Column(length = 20, nullable = false)
    private String businessNumber;

    @Column(length = 50, nullable = false)
    private String businessPic;

    @Column(length = 50, nullable = false)
    private String operationLicencePic;

    @Column(length = 20, nullable = false)
    private String corporateNumber;
}

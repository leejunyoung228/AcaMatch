package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.location.Dong;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @Column(nullable = false)
    private int premium;

    @Column(length = 50, nullable = false)
    private String acaName;

    @Column(length = 15, nullable = false)
    private String acaPhone;

    @Column(length = 300)
    private String comment;

    @Column
    private int teacherNum;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 50, nullable = false)
    private String detailAddress;

    @Column(length = 5, nullable = false)
    private String postNum;

    @Column(nullable = false)
    private int acaAgree;

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

package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.location.Dong;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
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

    @Column(length = 50, nullable = false)
    private String acaName;

    @Column(length = 15, nullable = false)
    private String acaPhone;

    @Column(length = 300)
    private String comment;

    private int teacherNum;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 50)
    private String acaPic;
}

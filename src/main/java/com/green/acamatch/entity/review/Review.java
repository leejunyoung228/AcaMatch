package com.green.acamatch.entity.review;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"join_class_id", "user_id"}) // 같은 JoinClass 내에서도 학부모와 학생이 각자 작성 가능
})
public class Review extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private double star;

    @Column(length = 5000)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "join_class_id", nullable = false)
    private JoinClass joinClass;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 보호자 또는 학생 계정
    private User user;

    @Column(nullable = false)
    private int banReview = 0;
}

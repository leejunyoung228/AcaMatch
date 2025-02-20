package com.green.acamatch.entity.review;

import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class ReviewPic extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 개별 PK 추가
    private Long picReviewId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Review 삭제 시, 사진도 삭제됨
    private Review review;

    @Column(name = "review_pic", nullable = false, length = 255)
    private String reviewPic; // 사진 URL 저장

}

package com.green.acamatch.entity.review;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class ReviewPicIds implements Serializable {
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_pic", nullable = false)
    private String reviewPic; // 사진 URL 저장

}

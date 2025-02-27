package com.green.acamatch.entity.review;

import com.green.acamatch.entity.academy.AcademyPicIds;
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

    @EmbeddedId
    private ReviewPicIds reviewPicIds;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @MapsId("reviewId")
    @OnDelete(action = OnDeleteAction.CASCADE) // Review 삭제 시, 사진도 삭제됨
    private Review review;

}

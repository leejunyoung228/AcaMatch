package com.green.acamatch.entity.like;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Like {

    @EmbeddedId
    private LikeIds likeIds;

    @ManyToOne
    @MapsId("userId")  // userId는 복합키의 일부로 사용
    private User user;

    @ManyToOne
    @MapsId("acaId")  // acaId도 복합키의 일부로 사용
    private Academy academy;
}

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
    @MapsId("userId") // userId를 EmbeddedId와 매핑
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("acaId") // acaId도 EmbeddedId와 매핑
    @JoinColumn(name = "aca_id", nullable = false, insertable = false, updatable = false)
    private Academy academy;
}

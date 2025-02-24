package com.green.acamatch.entity.like;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Like {
    @EmbeddedId
    private LikeIds likeIds;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;

}

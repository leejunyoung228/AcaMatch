package com.green.acamatch.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
public class SnsUser {
    @EmbeddedId
    private SnsUserId id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String snsUserId;

}

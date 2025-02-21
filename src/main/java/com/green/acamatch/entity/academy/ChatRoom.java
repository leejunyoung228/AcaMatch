package com.green.acamatch.entity.academy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.entity.user.User;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class ChatRoom {
    @Id
    @Tsid
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "aca_id", nullable = false)
    private Academy academy;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chatRoom")
    @JsonIgnore
    private List<Chat> chats;
}

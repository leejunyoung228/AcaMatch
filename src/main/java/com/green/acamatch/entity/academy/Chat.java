package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.myenum.SenderType;
import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class Chat extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(length = 100, nullable = false)
    private String message;

    @Column(nullable = false)
    private SenderType senderType;
    
    @Column(nullable = false)
    private boolean isRead = false;
}

package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "aca_id")
    private Academy academy;

    @Column(length = 100, nullable = false)
    private String message;

    private Integer senderType;
    private boolean isRead = false;
}

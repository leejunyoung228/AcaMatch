package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
public class Chat extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "aca_id")
    private Academy academy;

    @Column(length = 100, nullable = false)
    private String message;

    private Integer senderType;
    private Integer isRead = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(user.getUserId(), chat.user.getUserId()) &&
                Objects.equals(academy.getAcaId(), chat.academy.getAcaId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId(), academy.getAcaId());
    }
}

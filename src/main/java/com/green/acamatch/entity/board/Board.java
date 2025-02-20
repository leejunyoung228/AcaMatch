package com.green.acamatch.entity.board;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Board extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "aca_id")
    private Academy academy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int userType;

    @Column(length = 30, nullable = false)
    private String boardName;

    @Column(length = 1000, nullable = false)
    private String boardComment;
}
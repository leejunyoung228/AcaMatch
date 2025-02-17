package com.green.acamatch.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomDto {
    private long chatRoomId;
    private long userId;
    private String userName;
    private String userPic;
    private long acaId;
    private String acaName;
    private String acaPic;
    private LocalDateTime createdAt;
    private long unReadCount;
}

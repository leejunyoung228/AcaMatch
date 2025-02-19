package com.green.acamatch.chat.model;

import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

    public ChatRoomDto(ChatRoom chatRoom, AcademyPic academyPic, LocalDateTime createdAt, long unReadCount) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.userId = chatRoom.getUser().getUserId();
        this.userName = chatRoom.getUser().getName();
        this.userPic = chatRoom.getUser().getUserPic();
        this.acaId = chatRoom.getAcademy().getAcaId();
        this.acaName = chatRoom.getAcademy().getAcaName();
        this.acaPic = academyPic.getFeedPicIds().getAcaPic();
        this.createdAt = createdAt;
        this.unReadCount = unReadCount;
    }
}

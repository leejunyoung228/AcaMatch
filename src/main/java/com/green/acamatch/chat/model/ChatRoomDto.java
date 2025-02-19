package com.green.acamatch.chat.model;

import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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

    public ChatRoomDto(ChatRoom chatRoom, LocalDateTime createdAt, long unReadCount) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.userId = chatRoom.getUser().getUserId();
        this.userName = chatRoom.getUser().getName();
        this.userPic = chatRoom.getUser().getUserPic();
        this.acaId = chatRoom.getAcademy().getAcaId();
        this.acaName = chatRoom.getAcademy().getAcaName();
        this.acaPic = chatRoom.getAcademy().getAcademyPics().get(0).getFeedPicIds().getAcaPic();
        this.createdAt = createdAt;
        this.unReadCount = unReadCount;
    }
}

package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertChat(ChatSendReq req);
    List<ChatLogRes> selChatList(ChatReq req);

    List<ChatUserListRes> selUserList(ChatReq acaId);
}

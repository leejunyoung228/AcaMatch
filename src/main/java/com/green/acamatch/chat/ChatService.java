package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;

    public void sendMessage(ChatSendReq req) {
//        req.setUserId(AuthenticationFacade.getSignedUserId());
        chatMapper.insertChat(req);
    }

    public List<ChatLogRes> getQna(ChatReq req) {
        return chatMapper.selChatList(req);
    }

    public List<ChatUserListRes> getUserList(ChatReq req) {
        return chatMapper.selUserList(req);
    }
}

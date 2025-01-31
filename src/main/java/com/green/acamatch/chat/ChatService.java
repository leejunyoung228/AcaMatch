package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public void sendMessage(ChatSendReq req) {
        Chat chat = new Chat();
        Academy academy = new Academy();
        User user = new User();
        academy.setAcaId(req.getAcaId());
        user.setUserId(req.getUserId());
        chat.setAcademy(academy);
        chat.setUser(user);
        chat.setMessage(req.getMessage());
        chat.setSenderType(req.getSenderType());
        chatRepository.save(chat);
    }

    public List<ChatLogRes> getQna(ChatReq req) {
        User user = new User();
        user.setUserId(req.getUserId());
        Academy academy = new Academy();
        academy.setAcaId(req.getAcaId());
        List<Chat> res = chatRepository.findAllByUserAndAcademy(user, academy);
        return res.stream()
                .map(ChatLogRes::new) // ChatLogRes 생성자를 직접 매핑
                .collect(Collectors.toList());
    }

    public List<ChatUserListRes> getUserList(ChatReq req) {
        List<Chat> res;
        if (req.getUserId() != null) {
            User user = new User();
            user.setUserId(req.getUserId());
            res = chatRepository.findAllByUser(user);
        }
        if (req.getAcaId() != null) {}
        return null;
    }
}

package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        int userType = req.getUserId().equals(AuthenticationFacade.getSignedUserId()) ? 1 : 0;
        for (Chat chat : res) {
            if (chat.getIsRead() == 0 && chat.getSenderType() == userType) {  // 이미 읽지 않은 메시지에 대해서만
                chat.setIsRead(1);  // 읽음 상태로 업데이트
                chatRepository.save(chat); // 상태를 DB에 반영
            }
        }

        return res.stream()
                .map(ChatLogRes::new) // ChatLogRes 생성자를 직접 매핑
                .collect(Collectors.toList());
    }

    public List<ChatUserListRes> getUserList(ChatReq req) {
        List<Chat> res = new ArrayList<>();
        Pageable pageable = PageRequest.of(req.getStartIdx(), req.getSize());
        if (req.getUserId() != null) {
            User user = new User();
            user.setUserId(req.getUserId());
            res = chatRepository.findAllByUser(user, pageable);
        }
        if (req.getAcaId() != null) {
            Academy academy = new Academy();
            academy.setAcaId(req.getAcaId());
            res = chatRepository.findAllByAcademy(academy, pageable);
        }
        Set<Chat> unique_res = new HashSet<>(res);
        return unique_res.stream().map(ChatUserListRes::new).collect(Collectors.toList());
    }
}

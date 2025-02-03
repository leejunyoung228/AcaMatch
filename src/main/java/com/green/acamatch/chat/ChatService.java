package com.green.acamatch.chat;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final NotificationController notificationController;
    private final AcademyRepository academyRepository;
    private final UserUtils userUtils;

    public void sendMessage(ChatSendReq req) {
        Chat chat = new Chat();
        Academy academy = academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        User user = userUtils.findUserById(req.getUserId());
        academy.setAcaId(req.getAcaId());
        chat.setAcademy(academy);
        chat.setUser(user);
        chat.setMessage(req.getMessage());
        chat.setSenderType(req.getSenderType());
        chatRepository.save(chat);
        if(req.getSenderType() == 1) {
            notificationController.sendNotification(chat.getUser().getUserId(), "메세지가 도착했습니다");
        }
        if(req.getSenderType() == 0) {
            notificationController.sendNotification(chat.getAcademy().getUser().getUserId(), "메세지가 도착했습니다");
        }
    }

    public List<ChatLogList> getQna(ChatReq req) {
        User user = new User();
        user.setUserId(req.getUserId());
        Academy academy = new Academy();
        academy.setAcaId(req.getAcaId());
        Pageable pageable = PageRequest.of(req.getStartIdx(), req.getSize());
        List<Chat> res = chatRepository.findAllByUserAndAcademyOrderByCreatedAtDesc(user, academy, pageable);

        int userType = req.getUserId().equals(AuthenticationFacade.getSignedUserId()) ? 1 : 0;
        for (Chat chat : res) {
            if (chat.getIsRead() == 0 && chat.getSenderType() == userType) {  // 이미 읽지 않은 메시지에 대해서만
                chat.setIsRead(1);  // 읽음 상태로 업데이트
                chatRepository.save(chat); // 상태를 DB에 반영
            }
        }

        return res.stream()
                .map(ChatLogList::new) // ChatLogRes 생성자를 직접 매핑
                .collect(Collectors.toList());
    }

    public ChatUserRes getUserList(ChatReq req) {
        Page<Chat> res = null;
        Pageable pageable = PageRequest.of(req.getStartIdx(), req.getSize());
        if (req.getUserId() != null) {
            User user = new User();
            user.setUserId(req.getUserId());
            res = chatRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        }
        if (req.getAcaId() != null) {
            Academy academy = new Academy();
            academy.setAcaId(req.getAcaId());
            res = chatRepository.findAllByAcademyOrderByCreatedAtDesc(academy, pageable);
        }
        if (res == null) return ChatUserRes.builder().totalPages(0).users(new ArrayList<>()).build();
        Set<Chat> unique_res = new HashSet<>(res.getContent());
        return ChatUserRes.builder()
                .totalPages(res.getTotalPages())
                .users(unique_res.stream().map(ChatUserList::new).collect(Collectors.toList()))
                .build();
    }
}

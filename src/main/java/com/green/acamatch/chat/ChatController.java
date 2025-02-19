package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chat")
@Tag(name = "채팅")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("chat-room")
    public ResultResponse<ChatUserRes> getChatRoom(@ParameterObject @ModelAttribute ChatRoomGetReq req) {
        return ResultResponse.<ChatUserRes>builder()
                .resultMessage("조회 성공")
                .resultData(chatService.getChatUserList(req))
                .build();
    }

    @GetMapping("log")
    public ResultResponse<List<ChatLogList>> getChatLogList(@ParameterObject @ModelAttribute ChatLogGetReq req) {
        return ResultResponse.<List<ChatLogList>> builder()
                .resultMessage("조회 성공")
//                .resultData(chatService.getChatLog(req))
                .build();
    }

    @GetMapping("unread-message")
    @Operation(summary = "읽지 않은 메세지 개수 조회(로그인 필수)", description = "읽지 않은 메세지 가 있으면 true 없으면 false")
    public ResultResponse<Integer> unreadMessage() {
        return ResultResponse.<Integer>builder()
                .resultMessage("읽지 않은 메세지 개수 조회 성공")
                .resultData(chatService.checkUnreadMessage())
                .build();
    }

    @GetMapping
    public ResultResponse<Long>  getChatRoomId(@ParameterObject @ModelAttribute GetChatRoomIdReq req) {
        return ResultResponse.<Long> builder()
                .resultMessage("채팅방 id 조회 성공")
                .resultData(chatService.getChatRoomId(req))
                .build();
    }

    @GetMapping("{chatRoomId}")
    public ResultResponse<List<ChatLogList>> getChatLog(@PathVariable Long chatRoomId) {
        List<ChatLogList> res = chatService.getChatLog(chatRoomId);
        return ResultResponse.<List<ChatLogList>>builder()
                .resultMessage(String.format("%d rows", res.size()))
                .resultData(res).build();
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatSendReq req) {
        chatService.saveMessage(req);

        // 특정 유저 또는 학원에게 메시지 전송

    }


//    @PostMapping
//    @Operation(summary = "메세지 전송(로그인 필수)", description = "user-id,aca-id는 채팅하는 사람이 바뀌지 않으면 고정</br>sender-type : {0: user-> aca, 1: aca -> user}")
//    public ResultResponse<Integer> sendMessage(ChatSendReq req) {
//        chatService.sendMessage(req);
//        return ResultResponse.<Integer>builder().resultData(1).build();
//    }
//
//    @GetMapping("log")
//    @Operation(summary = "메세지 내역 조회(로그인 필수)", description = "user-id,aca-id는 필수")
//    public ResultResponse<List<ChatLogList>> getQnas(@ParameterObject @ModelAttribute ChatReq req) {
//        return ResultResponse.<List<ChatLogList>>builder().resultData(chatService.getQna(req)).build();
//    }
//
//    @GetMapping
//    @Operation(summary = "채팅방 목록(로그인 필수)", description = "user-id 또는 aca-id 중 하나만 보내주세요")
//    public ResultResponse<ChatUserRes> getUserList(@ParameterObject @ModelAttribute ChatReq req) {
//        return ResultResponse.<ChatUserRes>builder().resultData(chatService.getUserList(req)).build();
//    }
//
//

}

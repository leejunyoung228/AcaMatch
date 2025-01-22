package com.green.acamatch.chat;

import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chat")
public class ChatController {
    private final ChatService chatService;
    @PostMapping
    public ResultResponse<Integer> sendMessage(ChatSendReq req) {
        chatService.sendMessage(req);
        return ResultResponse.<Integer>builder().resultData(1).build();
    }

    @GetMapping("log")
    @Operation(description = "sender-type : {0: user-> aca, 1: aca -> user}")
    public ResultResponse<List<ChatLogRes>> getQnas(@ParameterObject @ModelAttribute ChatReq req) {
        return ResultResponse.<List<ChatLogRes>>builder().resultData(chatService.getQna(req)).build();
    }

    @GetMapping
    @Operation(description = "user-id 또는 aca-id 중 하나만 보내주세요")
    public ResultResponse<List<ChatUserListRes>> getUserList(@ParameterObject @ModelAttribute ChatReq req) {
        return ResultResponse.<List<ChatUserListRes>>builder().resultData(chatService.getUserList(req)).build();
    }
}

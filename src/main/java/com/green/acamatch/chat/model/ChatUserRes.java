package com.green.acamatch.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatUserRes {
    private int totalPages;
    private int totalElements;
    private List<ChatUserList> users;
}

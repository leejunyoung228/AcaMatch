package com.green.acamatch.user.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class RelationshipRes {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private LocalDate birth;
    private String userPic;
    private Integer certification;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

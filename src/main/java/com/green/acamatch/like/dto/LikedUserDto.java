package com.green.acamatch.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedUserDto {
    private Long acaId;
    private Long userId;
    private String userPic;
    private String nickName;
    private Integer academyAllLikeCount;
}

package com.green.acamatch.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedUserDto {
    private Long userId;
    private String userPic;
    private String nickName;
    @Schema(description = "학원에 달린 라이크의 총 갯수")
    private Integer academyAllLikeCount;
}

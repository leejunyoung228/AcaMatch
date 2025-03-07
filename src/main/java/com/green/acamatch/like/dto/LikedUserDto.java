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
    private Long academyAllLikeCount;

    // 올바른 생성자 추가
    public LikedUserDto(Long acaId, Long userId, String userPic, String nickName, Long academyAllLikeCount) {
        this.acaId = acaId;
        this.userId = userId;
        this.userPic = userPic;
        this.nickName = nickName;
        this.academyAllLikeCount = academyAllLikeCount;
    }
}


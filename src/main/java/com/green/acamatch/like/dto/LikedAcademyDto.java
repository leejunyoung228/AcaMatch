package com.green.acamatch.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedAcademyDto {
    private Long acaId;
    private String acaPic;
    private String acaName; // 학원 이름 필드 추가
    @Schema(description = "유저가 단 라이크의 총 갯수")
    private Integer userLikeCount;
    @Schema(description = "학원에 달린 라이크의 총 갯수")
    private Integer academyAllLikeCount;
}

package com.green.acamatch.like.dto;

import com.green.acamatch.entity.academy.AcademyPic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LikedAcademyDto {
    private Long acaId;
    private List<AcademyPic> pics;
    private String acaName; // 학원 이름 필드 추가
    @Schema(description = "유저가 단 라이크의 총 갯수")
    private Long userLikeCount;
    @Schema(description = "학원에 달린 라이크의 총 갯수")
    private Long academyAllLikeCount;


    // 기본 생성자 (JPA에서 필요)
    public LikedAcademyDto() {
    }

    // 모든 필드를 포함한 생성자 추가
    public LikedAcademyDto(Long acaId, String acaName, Long userLikesCount, Long academyLikesCount) {
        this.acaId = acaId;
        this.acaName = acaName;
        this.userLikeCount = userLikesCount;
        this.academyAllLikeCount = academyLikesCount;
    }
}

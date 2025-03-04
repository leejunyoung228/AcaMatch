package com.green.acamatch.like.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AcademyLikedUsersDto {
    private Long acaId;           // 학원 ID
    private String acaName;       // 학원명
    private Long academyAllLikeCount;  // 학원 좋아요 개수
    private List<LikedUserDto> likedUsers; // 좋아요한 유저 리스트

    // JPA @Query에서 사용할 생성자
    public AcademyLikedUsersDto(Long acaId, String acaName, Number academyAllLikeCount) {
        this.acaId = acaId;
        this.acaName = acaName;
        this.academyAllLikeCount = academyAllLikeCount != null ? academyAllLikeCount.longValue() : 0L;
        this.likedUsers = new ArrayList<>(); // 기본적으로 빈 리스트 초기화
    }
}

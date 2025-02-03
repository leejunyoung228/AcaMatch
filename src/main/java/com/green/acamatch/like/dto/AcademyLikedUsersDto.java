package com.green.acamatch.like.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AcademyLikedUsersDto {
    private Long acaId;           // 학원 ID
    private String acaName;       // 학원명
    private Integer academyAllLikeCount;  // 학원 좋아요 개수
    private List<LikedUserDto> likedUsers; // 좋아요한 유저 리스트
}
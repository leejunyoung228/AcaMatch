package com.green.acamatch.like.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedAcademyDto {
    private Long acaId;
    private String acaPic;
    private String acaName; // 학원 이름 필드 추가
}

package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAcademyListByDistanceRes {
    @Schema(title = "학원 PK")
    private long acaId;

    @Schema(title = "학원명")
    private String acaName;

    @Schema(title = "학원 사진")
    private String acaPic;

    @Schema(title = "학원 주소")
    private String address;

    @Schema(title = "상세 주소")
    private String detailAddress;

    @Schema(title = "우편 번호")
    private String postNum;

    @Schema(title = "평균 별점")
    private double starAvg;

    @Schema(title = "학원이 등록한 태그List", example = "국어, 영어...")
    private List<GetAcademyTagDto> tagNames;

    @Schema(title = "좋아요 수")
    private int likeCount;

    @Schema(title = "리뷰 수")
    private int reviewCount;

    @Schema(title = "학원과 거리")
    private double distance;
}

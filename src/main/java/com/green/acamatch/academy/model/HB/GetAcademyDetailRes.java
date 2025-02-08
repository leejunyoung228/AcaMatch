package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.green.acamatch.academy.model.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class GetAcademyDetailRes {
    @Schema(title = "학원 PK")
    private Long acaId;
    @Schema(title = "학원 이름")
    private String acaName;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "학원 주소")
    private String address;
    @Schema(title = "학원 전화번호")
    private String acaPhone;
    @Schema(title = "강사 수")
    private Integer teacherNum;
    @Schema(title = "학원 설명")
    private String comments;
    @Schema(title = "별점")
    private Double star;
    @Schema(title = "리뷰 수")
    private Integer reviewCount;
    @Schema(title = "좋아요 수")
    private Integer likeCount;
    @Schema(title = "로그인된 사용자가 좋아요를 했는지 안했는지 여부", example = "true or false")
    private Boolean isLiked;
    @Schema(title = "모든 주소")
    private AddressDto addressDto;
    private List<GetAcademyTagDto> tagName;
    @Schema(title = "학원 정보")
    private List<GetAcademyDetailClassDto> classes;
    @Schema(title = "리뷰 정보")
    private List<ReviewDto> reviews;
}
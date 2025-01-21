package com.green.studybridge.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class getAcademyRes {
    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    @Schema(title = "학원 사진", example = "학원.jpg")
    private String acaPic;

    @Schema(title = "학원 이름", example = "그린컴퓨터아트학원")
    private String acaName;

    @Schema(title = "학원 주소", example = "동성로1길 15 5층")
    private String address;

    @Schema(title = "학원 별점", example = "4.5")
    private double star;

    @Schema(title = "태그 PK", example = "1")
    private long tagId;

    @Schema(title = "학원이 등록한 태그List", example = "국어, 영어...")
    private List<GetAcademyTagDto> tagName;
}

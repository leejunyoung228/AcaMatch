package com.green.studybridge.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AcademyUpdateReq {
    private long acaId;
    private long userId;

    @Schema(title = "학원이름")
    private String acaName;
    @Schema(title = "학원전화번호")
    private String acaPhone;
    @Schema(title = "학원내용")
    private String comment;
    @Schema(title = "강사 수")
    private int teacherNum;
    @Schema(title = "학원오픈시간")
    private String openTime;
    @Schema(title = "학원마감시간")
    private String closeTime;
    @Schema(title = "학원주소")
    private String address;
    @Schema(title = "학원사진")
    private String acaPic;
    @Schema(title = "태그Id 리스트")
    private List<Long> tagIdList;
}

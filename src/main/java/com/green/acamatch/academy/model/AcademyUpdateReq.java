package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AcademyUpdateReq {
    private long acaId;
    private long userId;
    private long dongId;

    @Schema(title = "학원이름")
    private String acaName;
    @Schema(title = "학원전화번호")
    @Pattern(regexp = "^(0[0-9][0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String acaPhone;
    @Schema(title = "학원내용")
    private String comment;
    @Schema(title = "강사 수")
    private int teacherNum;
    @Schema(title = "학원오픈시간")
    private LocalTime openTime;
    @Schema(title = "학원마감시간")
    private LocalTime closeTime;
    @Schema(title = "학원주소")
    private String address;
    @Schema(title = "학원사진")
    private String acaPic;
    @Schema(title = "태그Id 리스트")
    private List<Long> tagIdList;
}

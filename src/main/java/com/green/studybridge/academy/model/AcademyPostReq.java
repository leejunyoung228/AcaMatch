package com.green.studybridge.academy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Schema(title = "학원정보등록")
public class AcademyPostReq {
    @JsonIgnore
    private long acaId;

    private long userId;

    private long dongId;

    @Schema(title = "학원이름", example = "영재수학학원", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaName;
    @Schema(title = "학원전화번호", example = "01012341234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaPhone;
    @Schema(title = "학원내용", example = "여기는 수학전문학원입니다.")
    private String comment;
    @Schema(title = "강사 수", example = "3")
    private int teacherNum;

    @Schema(title = "오픈시간", example = "9:00")
    @JsonFormat(pattern = "HH:mm")
    private String openTime;

    @Schema(title = "마감시간", example = "20:00")
    @JsonFormat(pattern = "HH:mm")
    private String closeTime;

    @Schema(title = "학원주소", example = "대구 달서천로100길 10-100", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
    @Schema(title = "학원사진")
    private String acaPic;

    @Schema(title = "수강 연령대 리스트")
    private List<Long> ageRangeIdList;
    @Schema(title = "수준 리스트")
    private List<Long> levelIdList;
    @Schema(title = "요일 리스트")
    private List<Long> daysIdList;

    @Schema(title = "태그Id 리스트")
    private List<Long> tagIdList;
}

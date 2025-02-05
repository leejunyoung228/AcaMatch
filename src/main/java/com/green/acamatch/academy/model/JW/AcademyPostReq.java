package com.green.acamatch.academy.model.JW;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.academy.model.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@Schema(title = "학원정보등록")
public class AcademyPostReq {
    @JsonIgnore
    private long acaId;

    @JsonIgnore
    private long dongId;

    private long userId;


    @NotEmpty
    @Schema(title = "학원이름", example = "영재수학학원", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaName;
    @NotEmpty
    @Schema(title = "학원전화번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^(0[0-9][0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String acaPhone;

    @Schema(title = "학원내용", example = "여기는 수학전문학원입니다.")
    private String comment;
    @Schema(title = "강사 수", example = "3")
    private int teacherNum;

    @Schema(title = "오픈시간", example = "9:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Schema(title = "마감시간", example = "20:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonIgnore
    @Schema(title = "학원주소", example = "대구 중구 중앙대로394", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
    @Valid
    private AddressDto addressDto;

    @JsonIgnore
    @Schema(title = "학원사진")
    private String acaPic;



    @Schema(title = "태그Id 리스트")
    private List<Long> tagIdList;
}

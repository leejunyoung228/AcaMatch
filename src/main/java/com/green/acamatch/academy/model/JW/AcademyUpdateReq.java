package com.green.acamatch.academy.model.JW;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.academy.model.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AcademyUpdateReq {
    private long acaId;
    private long userId;
    @JsonIgnore
    private long dongId;

    @Schema(title = "학원이름")
    private String acaName;
    @Schema(title = "학원전화번호")
    @Pattern(regexp = "^(0[0-9][0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String acaPhone;
    @Schema(title = "학원내용")
    private String comment;
    @Schema(title = "강사 수")
    private Integer teacherNum;

    /*@Schema(title = "학원주소")
    private AddressDto addressDto;*/

    /*@Schema(title = "학원사진")
    private String acaPic;*/



    @Schema(title = "오픈시간", example = "10:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Schema(title = "마감시간", example = "20:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @Schema(title = "학원주소", example = "대구 중구 중앙대로395")
    private String address;

    @Schema(title = "학원상세주소", example = "그린빌딩 3층 301호")
    private String detailAddress;

    @Schema(title = "우편번호", example = "234-56")
    private String postNum;

    @Schema(title = "학원정보등록승인여부", example = "1: 승인, 0: 미승인")
    private Integer acaAgree;

    @Schema(title = "태그Id 리스트")
    private List<String> tagNameList;

    @Schema(title = "프리미엄 학원 여부")
    private Integer premium;

    @Schema(title = "위도")
    private Double lat;

    @Schema(title = "경도")
    private Double lon;

    @Schema(title = "사업자명")
    private String businessName;

    @Schema(title = "사업자번호")
    private String businessNumber;

    @Schema(title = "법인번호")
    private String corporateNumber;

}

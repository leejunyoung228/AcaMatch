package com.green.acamatch.academy.model.JW;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.academy.model.AddressDto;
import com.green.acamatch.entity.tag.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Schema(title = "유저pk")
    private long userId;


    @NotEmpty(message = "학원이름은 필수입니다.")
    @Schema(title = "학원이름", example = "영재수학학원", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaName;

    @NotEmpty(message = "전화번호는 필수입니다.")
    @Schema(title = "학원전화번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^(0(10|2|3[1-3]|4[1-3]|5[1-5]|6[1-4]))-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String acaPhone;

    @Schema(title = "학원내용", example = "여기는 수학전문학원입니다.")
    private String comment;
    @Schema(title = "강사 수", example = "3")
    private Integer teacherNum;

    @Schema(title = "오픈시간", example = "9:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Schema(title = "마감시간", example = "20:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @NotEmpty(message = "학원주소는 필수입니다.")
    @Schema(title = "학원주소", example = "대구 중구 중앙대로394", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotEmpty(message = "학원상세주소는 필수입니다.")
    @Schema(title = "학원상세주소", example = "그린빌딩 5층 501호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String detailAddress;

    @NotEmpty(message = "우편번호는 필수입니다.")
    @Schema(title = "우편번호", example = "456-78", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postNum;

    /*@Schema(title = "학원정보등록승인여부", example = "1: 승인, 0: 미승인")
    private int acaAgree;*/

    /*@Valid
    private AddressDto addressDto;*/


    @Schema(title = "학원사진")
    private List<String> acaPic;

    @Schema(title = "태그이름 리스트")
    private List<String> tagNameList;

    /*@JsonIgnore
    @Schema(title = "프리미엄학원 여부")
    private int premium;*/

    @JsonIgnore
    @Schema(title = "위도", requiredMode = Schema.RequiredMode.REQUIRED)
    private double lat;

    @JsonIgnore
    @Schema(title = "경도", requiredMode = Schema.RequiredMode.REQUIRED)
    private double lon;

    @NotEmpty(message = "사업자명은 필수입니다.")
    @Schema(title = "사업자명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessName;

    @NotEmpty(message = "사업자번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{10}$", message = "사업자등록번호는 10자리 숫자여야 합니다.")
    @Schema(title = "사업자번호", example = "641-77-00292", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessNumber;


    @Schema(title = "사업자등록증", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessPic;


    @Schema(title = "학원운영증", requiredMode = Schema.RequiredMode.REQUIRED)
    private String operationLicencePic;

    @Schema(title = "법인번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String corporateNumber;



}

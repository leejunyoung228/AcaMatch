package com.green.acamatch.academy.model.HB;

import com.green.acamatch.academy.model.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyDetail {
    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    @Schema(title = "학원 전화번호", example = "053-1234-1234")
    private String acaPhone;

    @Schema(title = "학원 이름", example = "그린컴퓨터아트학원")
    private String acaName;

    @Schema(title = "학원 사진", example = "green.jpg")
    private String acaPic;

    @Schema(title = "학원 설명", example = "컴퓨터 가르치는 학원입니다.")
    private String comment;

    @Schema(title = "강사 수", example = "10")
    private int teacherNum;

    @Schema(title = "학원 오픈 시간", example = "09:00")
    private String openTime;

    @Schema(title = "학원 마감 시간", example = "18:00")
    private String closeTime;

    @Schema(title = "학원 상세 주소", example = "대구광역시 중구 109-2")
    private String address;

    private AddressDto addressDto;
}

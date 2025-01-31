package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDto {
    @NotEmpty
    @Schema(title = "기본주소", example = "대구 중구 중앙대로394")
    private String address;
    @NotEmpty
    @Schema(title = "상세주소", example = "그린빌딩 5층 502호")
    private String detailAddress;
    @NotEmpty
    @Schema(title = "우편번호", example = "45678")
    private String postNum;
}

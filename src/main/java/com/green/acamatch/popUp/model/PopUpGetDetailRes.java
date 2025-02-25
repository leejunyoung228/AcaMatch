package com.green.acamatch.popUp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PopUpGetDetailRes {
    @Schema(title = "팝업 제목", example = "아카매치 GRAND OPEN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(title = "팝업 내용")
    private String comment;
    @Schema(title = "팝업 사진")
    private String popUpPic;
}
package com.green.acamatch.joinClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.entity.myenum.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinClassDto {
    @Schema(title = "학원 PK", example = "1611", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "학원 사진 여러장")
    private String acaPics;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "학원 이름", example = "비탑영어학원")
    private String acaName;

    private List<JoinClassInfoDto> classList;
}
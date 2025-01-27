package com.green.acamatch.joinClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassDto {
    @JsonIgnore
    private long acaId;
    @JsonIgnore
    private long classId;

    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "학원 이름", example = "비탑영어학원")
    private String acaName;
    @Schema(title = "수업 이름", example = "초등 영어")
    private String className;
}

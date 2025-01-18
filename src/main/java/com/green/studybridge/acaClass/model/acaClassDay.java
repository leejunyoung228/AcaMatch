package com.green.studybridge.acaClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class acaClassDay {
    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @JsonIgnore
    private long dayId;
    @Schema(title = "요일", example = "월", requiredMode = Schema.RequiredMode.REQUIRED)
    private String day;
}

package com.green.acamatch.acaClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeekDays {
    @JsonIgnore
    private long dayId;
    @Schema(title = "요일", example = "월", requiredMode = Schema.RequiredMode.REQUIRED)
    private String day;
}
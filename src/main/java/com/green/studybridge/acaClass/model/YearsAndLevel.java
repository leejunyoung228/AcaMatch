package com.green.studybridge.acaClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YearsAndLevel {
    @JsonIgnore
    private long categoryId;

    @Schema(title = "YearsAndLevel")
    private String categoryName;
}
package com.green.studybridge.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostAcademySearch {
    @JsonIgnore
    private long searchId;

    @Schema(title = "태그 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long tagId;
}

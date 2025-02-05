package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAcademyTagDto {
    @Schema(title = "태그 PK", example = "1")
    private long tagId;

    @Schema(title = "태그명" , example = "초등 영어, 중등 영어...")
    private List<String> tagName;
}
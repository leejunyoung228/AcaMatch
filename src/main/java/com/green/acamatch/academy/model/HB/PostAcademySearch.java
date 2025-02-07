package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;

@Getter
@Setter
@EqualsAndHashCode
public class PostAcademySearch {
    @JsonIgnore
    private long searchId;

    @Schema(title = "태그 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long tagId;
}

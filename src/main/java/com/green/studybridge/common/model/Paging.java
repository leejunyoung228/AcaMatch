package com.green.studybridge.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Paging {
    private final static int DEFAULT_PAGE = 1;
    private final static int DEFAULT_PAGE_SIZE = 20;
    @Positive
    @Schema(example = "1", description = "Selected Page")
    private int page;
    @Positive
    @Schema(example = "30", description = "item count per page")
    private int size;
    @JsonIgnore
    private int startIdx;

    public Paging(Integer page, Integer size) {
        this.page = (page == null || page < 1) ? DEFAULT_PAGE : page;
        this.size = (size == null || size < 1) ? DEFAULT_PAGE_SIZE : size;
        this.startIdx = (this.page - 1) * this.size;
    }
}

package com.green.acamatch.config.model;

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

    // 기본 생성자 추가
    public Paging() {
        this.page = DEFAULT_PAGE;
        this.size = DEFAULT_PAGE_SIZE;
        this.startIdx = (this.page - 1) * this.size;
    }

    public Paging(Integer page, Integer size) {
        this.page = (page == null || page < 1) ? DEFAULT_PAGE : page;
        this.size = (size == null || size < 1) ? DEFAULT_PAGE_SIZE : size;
        this.startIdx = (this.page - 1) * this.size;
    }
}

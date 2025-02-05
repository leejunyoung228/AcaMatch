package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyAcademyReviewListGetReq extends Paging {

    @JsonIgnore
    private Long acaId;

    @Schema(title = "작성자 ID", description = "리뷰 작성자의 유저 ID", example = "1")
    private Long userId;

    public MyAcademyReviewListGetReq(Integer page, Integer size) {
        super(page, size);
    }
    public Long getAcaId() {
        return acaId != null ? acaId : 0L; // ✅ acaId가 null이면 0L 반환 (SQL에서 처리 가능)
    }
}


package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyDetailReq extends Paging {
    @Schema(title = "로그인한 유저 PK", example = "1")
    private Integer signedUserId;

    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    @Schema(title = "일반 리뷰 페이징 시작 인덱스", example = "0")
    private Integer generalStartIdx;

    @Schema(title = "미디어 포함 리뷰 페이징 시작 인덱스", example = "0")
    private Integer mediaStartIdx;

    public GetAcademyDetailReq(Integer page, Integer size, Integer signedUserId, long acaId, Integer generalStartIdx, Integer mediaStartIdx) {
        super(page, size);
        this.signedUserId = signedUserId == null ? 0 : signedUserId;
        this.acaId = acaId;
        this.generalStartIdx = generalStartIdx == null ? 0 : generalStartIdx;
        this.mediaStartIdx = mediaStartIdx == null ? 0 : mediaStartIdx;
    }
}

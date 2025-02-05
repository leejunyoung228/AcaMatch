package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetAcademyDetailReq extends Paging{
    @Schema(title = "로그인한 유저 PK", example = "1")
    private Integer signedUserId;

    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    public GetAcademyDetailReq(Integer page, Integer size, Integer signedUserId, long acaId) {
        super(page, size);
        this.signedUserId = signedUserId == null ? 0 : signedUserId;
        this.acaId = acaId;
    }
}

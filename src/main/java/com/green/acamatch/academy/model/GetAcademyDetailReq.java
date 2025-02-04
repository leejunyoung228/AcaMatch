package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetAcademyDetailReq extends Paging{
    @Schema(title = "로그인한 유저 PK", example = "2119")
    private long signedUserId;

    @Schema(title = "학원 PK", example = "277")
    private long acaId;

    public GetAcademyDetailReq(Integer page, Integer size, long signedUserId, long acaId, Integer startIdx, Integer size1) {
        super(page, size);
        this.signedUserId = signedUserId;
        this.acaId = acaId;
    }
}

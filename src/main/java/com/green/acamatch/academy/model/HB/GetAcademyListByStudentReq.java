package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByStudentReq extends Paging {
    @Schema(title = "로그인한 사람의 PK", example = "1")
    private long signedUserId;

    public GetAcademyListByStudentReq(Integer page, Integer size, long signedUserId) {
        super(page, size);
        this.signedUserId = signedUserId;
    }
}
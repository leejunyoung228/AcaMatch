package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByAcaNameOrderTypeReq extends Paging {
    @Schema(title = "학원 이름")
    private String acaName;

    @Schema(title = "주문 타입", description = "0이면 학원, 1이면 교재")
    private Integer orderType;

    @Schema(title = "시작일", example = "2025-02-01")
    private String startDate;

    @Schema(title = "종료일", example = "2025-03-31")
    private String endDate;

    @Schema(title = "학원 PK")
    private Long acaId;

    @Schema(title = "유저 PK", description = "학원 관계자 입장에서 쓰실때만 넣으시면 됩니다!")
    private Long userId;

    public GetAcademyListByAcaNameOrderTypeReq(Integer page, Integer size) {
        super(page, size);
    }
}

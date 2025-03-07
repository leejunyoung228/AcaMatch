package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByDistanceReq extends Paging {
    @Schema(title = "위도", example = "35.868364")
    private double lat;

    @Schema(title = "경도", example = "128.594089")
    private double lon;

    public GetAcademyListByDistanceReq(Integer page, Integer size) {
        super(page, size);
    }
}

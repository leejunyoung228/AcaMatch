package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDongRes {
    @Schema(title = "동 PK", example = "281, 282, 283, 284")
    private long dongId;

    @Schema(title = "동 이름", example = "쌍문동, 방학동, 창동, 도봉동")
    private String dongName;
}

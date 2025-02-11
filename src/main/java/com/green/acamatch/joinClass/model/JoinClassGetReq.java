package com.green.acamatch.joinClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassGetReq extends Paging {
    @Schema(title = "사용자 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(title = "역할", description = "1 = 학생 , 2 = 부모", requiredMode = Schema.RequiredMode.REQUIRED)
    private int role;

    public JoinClassGetReq(Integer page, Integer size, Long userId, int role) {
        super(page, size);
        this.userId = userId;
        this.role = role;
    }
}
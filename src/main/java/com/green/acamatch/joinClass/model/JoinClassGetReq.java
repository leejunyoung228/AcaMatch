package com.green.acamatch.joinClass.model;

import com.green.acamatch.config.model.Paging;
import com.green.acamatch.entity.myenum.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassGetReq extends Paging {
    @Schema(title = "사용자 PK", example = "1")
    private Long studentId;
    @Schema(title = "부모 PK", example = "1")
    private Long parentId;

    public JoinClassGetReq(Integer page, Integer size, Long studentId, Long parentId) {
        super(page, size);
        this.studentId= studentId;
        this.parentId = parentId;
    }
}
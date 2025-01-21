package com.green.studybridge.academy.model;

import com.green.studybridge.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademySelOrderByLikeReq extends Paging {
    public AcademySelOrderByLikeReq(Integer page, Integer size) {
        super(page, size);
    }
}

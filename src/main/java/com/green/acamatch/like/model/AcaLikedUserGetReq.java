package com.green.acamatch.like.model;

import com.green.acamatch.config.model.Paging;
import com.green.acamatch.like.dto.LikedUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AcaLikedUserGetReq extends Paging {
    private long acaId; // 학원 ID
    private List<LikedUserDto> likedUsers; // 학원을 좋아요한 유저 목록

    public AcaLikedUserGetReq(Integer page, Integer size) {
        super(page, size);
    }
}
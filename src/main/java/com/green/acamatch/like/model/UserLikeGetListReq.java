package com.green.acamatch.like.model;

import com.green.acamatch.config.model.Paging;
import com.green.acamatch.like.dto.LikedAcademyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserLikeGetListReq extends Paging {
    private Long userId; // 유저 ID
    private List<LikedAcademyDto> likedAcademies; // 유저가 좋아요한 학원 목록

    public UserLikeGetListReq(Integer page, Integer size) {
        super(page, size);
    }
}
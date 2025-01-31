package com.green.acamatch.like.model;

import com.green.acamatch.like.dto.LikedUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class AcaLikeRes {
    private List<LikedUserDto> likedUsers; // LikedUserDto 사용
    private boolean isLiked;
}
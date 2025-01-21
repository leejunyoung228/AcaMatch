package com.green.acamatch.like;


import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.AcaDelLikeReq;
import com.green.acamatch.like.model.AcaLikeReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface LikeMapper {


    // 좋아요 등록
    int insertLike(AcaLikeReq req);

    // 좋아요 삭제
    int deleteLike(AcaDelLikeReq req);

    // 특정 학원의 좋아요를 누른 유저들의 프로필 사진 조회 및 리스트 목록
    List<LikedUserDto> getLikedUserDetails(long acaId);

    // 특정 유저가 좋아요한 학원 목록 조회
    List<Long> getUserLikedAcademies(long userId);

    //특정 유저가 좋아요한 학원의 사진 리스트 조회
    List<LikedAcademyDto> getUserLikesWithPics(long userId);

    // 특정 유저 ID가 존재하는지 확인
    int checkUserExists(long userId);
    // 특정 학원 ID가 존재하는지 확인
    int checkAcaExists (long acaId);

    // 본인이 관리하는 학원인지 확인
    boolean isUserManagingAcademy(long userId, long acaId);


}

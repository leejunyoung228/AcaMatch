package com.green.studybridge.like;

import com.green.studybridge.like.dto.LikedAcademyDto;
import com.green.studybridge.like.dto.LikedUserDto;
import com.green.studybridge.like.model.AcaDelLikeReq;
import com.green.studybridge.like.model.AcaLikeReq;
import com.green.studybridge.like.model.AcaLikeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;

    // 좋아요 등록
    public AcaLikeRes addLike(AcaLikeReq req) {
        // 좋아요 등록
        mapper.insertLike(req);

        // 다른 유저들의 사진 리스트를 불러오지 않고 결과만 반환
        return new AcaLikeRes(null, true); // likedUsers는 null로 설정
    }



    // 좋아요 삭제
    public AcaLikeRes removeLike(AcaDelLikeReq req) {
        // 좋아요 삭제
        mapper.deleteLike(req);

        // 추가 데이터 조회 없이 결과만 반환
        return new AcaLikeRes(null, false); // likedUsers는 null로 설정
    }


    //특정 학원에 좋아요 한 유저와 유저 사진 조회
    public List<LikedUserDto> getLikedUserDetails(long acaId) {
        return mapper.getLikedUserDetails(acaId);
    }

    //특정 유저가 좋아요한 학원의 사진과 학원 리스트 조회
    public List<LikedAcademyDto> getUserLikesWithPics(long userId) {
        return mapper.getUserLikesWithPics(userId);
    }
}

package com.green.acamatch.like;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;
    private final UserMessage userMessage;

    // 좋아요 등록
    public AcaLikeRes addLike(AcaLikeReq req) {
        // 유저 존재 여부 검증
        if (checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return new AcaLikeRes(null, false);
        }
        // 학원 존재 여부 검증
        if (checkAcaExists(req.getAcaId()) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return new AcaLikeRes(null, false);
        }

        // 중복 여부 확인
        int likeExists = mapper.checkLikeExists(req.getUserId(), req.getAcaId());
        if (likeExists > 0) {
            userMessage.setMessage("이미 좋아요를 누른 상태입니다.");
            return new AcaLikeRes(null, false);
        }

        // 본인이 관리하는 학원인지 확인
        if (mapper.isUserManagingAcademy(req.getUserId(), req.getAcaId())) {
            userMessage.setMessage("본인이 관리하는 학원에는 좋아요를 누를 수 없습니다.");
            return new AcaLikeRes(null, false);
        }

        try {
            int result = mapper.insertLike(req);
            if (result == 0) {
                log.warn("좋아요 등록 실패: userId={}, acaId={}", req.getUserId(), req.getAcaId());
                userMessage.setMessage("좋아요 등록에 실패했습니다.");
                return new AcaLikeRes(null, false);
            }
        } catch (Exception e) {
            log.error("좋아요 등록 중 예외 발생: {}", e.getMessage(), e);
            userMessage.setMessage("좋아요 등록 중 오류가 발생했습니다.");
            return new AcaLikeRes(null, false);
        }

        userMessage.setMessage("좋아요가 성공적으로 등록되었습니다.");
        return new AcaLikeRes(null, true);
    }

    // 좋아요 삭제
    @Transactional
    public AcaLikeRes removeLike(AcaDelLikeReq req) {
        // 유저 존재 여부 검증
        if (checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return new AcaLikeRes(null, false);
        }

        // 학원 존재 여부 검증
        if (checkAcaExists(req.getAcaId()) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return new AcaLikeRes(null, false);
        }

        // 중복 여부 확인
        int likeExists = mapper.checkLikeExists(req.getUserId(), req.getAcaId());
        if (likeExists == 0) {
            userMessage.setMessage("좋아요를 누르지 않은 상태입니다.");
            return new AcaLikeRes(null, false);
        }

        try {
            int rowsAffected = mapper.deleteLike(req);
            if (rowsAffected == 0) {
                userMessage.setMessage("좋아요 삭제에 실패했습니다.");
                return new AcaLikeRes(null, false);
            }
        } catch (Exception e) {
            log.error("좋아요 삭제 중 예외 발생: {}", e.getMessage(), e);
            userMessage.setMessage("좋아요 삭제 중 오류가 발생했습니다.");
            return new AcaLikeRes(null, false);
        }

        userMessage.setMessage("좋아요가 성공적으로 삭제되었습니다.");
        return new AcaLikeRes(null, true);
    }

    // 특정 학원에 좋아요 한 유저와 유저 사진 조회
    public List<LikedUserDto> getLikedUserDetails(AcaLikedUserGetReq req) {
        // 학원 존재 여부 검증
        if (checkAcaExists(req.getAcaId()) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return Collections.emptyList();
        }

        try {
            List<LikedUserDto> likedUsers = mapper.getLikedUserDetails(req);
            if (likedUsers == null || likedUsers.isEmpty()) {
                userMessage.setMessage("해당 학원에 좋아요를 누른 유저가 없습니다.");
                return Collections.emptyList();
            }
            userMessage.setMessage("좋아요를 누른 유저 목록 조회가 완료되었습니다.");
            return likedUsers;
        } catch (Exception e) {
            log.error("좋아요 유저 목록 조회 중 예외 발생: {}", e.getMessage(), e);
            userMessage.setMessage("좋아요 유저 목록 조회 중 오류가 발생했습니다.");
            return Collections.emptyList();
        }
    }

    // 특정 유저가 좋아요한 학원의 사진과 학원 리스트 조회
    public List<LikedAcademyDto> getUserLikesWithPics(UserLikeGetListReq req) {
        // 유저 존재 여부 검증
        if (checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return Collections.emptyList();
        }

        try {
            List<LikedAcademyDto> likedAcademies = mapper.getUserLikesWithPics(req);
            if (likedAcademies == null || likedAcademies.isEmpty()) {
                userMessage.setMessage("해당 유저가 좋아요한 학원이 없습니다.");
                return Collections.emptyList();
            }
            userMessage.setMessage("해당 유저가 좋아요한 학원 목록 조회가 완료되었습니다.");
            return likedAcademies;
        } catch (Exception e) {
            log.error("좋아요 학원 목록 조회 중 예외 발생: {}", e.getMessage(), e);
            userMessage.setMessage("좋아요 학원 목록 조회 중 오류가 발생했습니다.");
            return Collections.emptyList();
        }
    }

    // 유저 존재 여부 확인
    public int checkUserExists(long userId) {
        if (userId <= 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return 0;
        }

        int count = mapper.checkUserExists(userId);
        log.debug("유저 ID 검증: userId={}, count={}", userId, count);
        return count;
    }

    // 학원 존재 여부 확인
    public int checkAcaExists(long acaId) {
        if (acaId <= 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return 0;
        }

        int count = mapper.checkAcaExists(acaId);
        log.debug("학원 ID 검증: acaId={}, count={}", acaId, count);
        return count;
    }
}
package com.green.acamatch.like;

import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.ReviewErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.like.dto.AcademyLikedUsersDto;
import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;
    private final UserMessage userMessage;

    /**
     * JWT에서 userId 가져오기
     */
    private JwtUser getAuthenticatedUser() {
        log.debug("getAuthenticatedUser() 호출됨");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("인증 실패: 인증 정보 없음 또는 익명 사용자");
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        Object principal = authentication.getPrincipal();
        log.debug("인증된 사용자 정보: {}", principal);

        if (principal instanceof JwtUser) {
            return (JwtUser) principal;
        } else if (principal instanceof String) {
            try {
                Long userId = Long.parseLong((String) principal);
                return new JwtUser(userId, Collections.emptyList());
            } catch (NumberFormatException e) {
                log.error("JWT userId 파싱 실패: {}", principal);
                throw new CustomException(UserErrorCode.UNAUTHENTICATED);
            }
        } else {
            log.warn("인증된 사용자 타입이 올바르지 않음: {}", principal.getClass().getName());
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * 좋아요 등록
     */
    public AcaLikeRes addLike(AcaLikeReq req) {
        JwtUser jwtUser = getAuthenticatedUser();
        long jwtUserId = jwtUser.getSignedUserId();
        long requestUserId = req.getUserId();
        long acaId = req.getAcaId();

        log.debug("좋아요 등록 요청: JWT userId={}, 요청 바디 userId={}, acaId={}", jwtUserId, requestUserId, acaId);

        if (jwtUserId != requestUserId) {
            userMessage.setMessage("로그인 된 유저가 아닙니다. 권한이 없습니다.");
            return new AcaLikeRes(false);
        }

        if (checkUserExists(requestUserId) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return new AcaLikeRes( false);
        }

        if (checkAcaExists(acaId) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return new AcaLikeRes(false);
        }

        if (mapper.checkLikeExists(requestUserId, acaId) > 0) {
            userMessage.setMessage("이미 좋아요를 누른 상태입니다.");
            return new AcaLikeRes(false);
        }

//        if (mapper.isUserManagingAcademy(requestUserId, acaId)) {
//            userMessage.setMessage("본인이 관리하는 학원에는 좋아요를 누를 수 없습니다.");
//            return new AcaLikeRes( false);
//        }

        try {
            int result = mapper.insertLike(req);
            if (result == 0) {
                userMessage.setMessage("좋아요 등록에 실패했습니다.");
                return new AcaLikeRes( false);
            }
        } catch (Exception e) {
            userMessage.setMessage("좋아요 등록 중 오류가 발생했습니다.");
            return new AcaLikeRes(false);
        }

        userMessage.setMessage("좋아요가 성공적으로 등록되었습니다.");
        return new AcaLikeRes(true);
    }

    /**
     * 좋아요 삭제
     */
    public AcaLikeRes removeLike(AcaDelLikeReq req) {
        JwtUser jwtUser = getAuthenticatedUser();
        long jwtUserId = jwtUser.getSignedUserId();
        long requestUserId = req.getUserId();
        long acaId = req.getAcaId();

        if (jwtUserId != requestUserId) {
            userMessage.setMessage("로그인 된 유저가 아닙니다. 권한이 없습니다.");
            return new AcaLikeRes(false);
        }

        if (checkUserExists(requestUserId) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return new AcaLikeRes(false);
        }

        if (checkAcaExists(acaId) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return new AcaLikeRes(false);
        }

        if (mapper.checkLikeExists(requestUserId, acaId) == 0) {
            userMessage.setMessage("좋아요를 누르지 않은 상태입니다.");
            return new AcaLikeRes(false);
        }

        try {
            int rowsAffected = mapper.deleteLike(req);
            if (rowsAffected == 0) {
                userMessage.setMessage("좋아요 삭제에 실패했습니다.");
                return new AcaLikeRes( false);
            }
        } catch (Exception e) {
            userMessage.setMessage("좋아요 삭제 중 오류가 발생했습니다.");
            return new AcaLikeRes(false);
        }

        userMessage.setMessage("좋아요가 성공적으로 삭제되었습니다.");
        return new AcaLikeRes(true);
    }

    /**
     * 특정 학원에 좋아요한 유저 목록 조회
     */
    /**
     *  특정 유저가 학원 관계자인지 확인 (소유한 학원이 있는지 체크)
     */
    public List<Long> getOwnedAcademyIds(Long userId) {
        return mapper.getOwnedAcademyIds(userId); // 학원 ID 리스트 반환 (없으면 비관계자)
    }

    /**
     *  학원 관계자가 소유한 모든 학원의 좋아요 유저 목록 조회
     */
    public List<AcademyLikedUsersDto> getAllOwnedAcademyLikes(AcaLikedUserGetReq req) {
        JwtUser jwtUser = getAuthenticatedUser();
        long userId = jwtUser.getSignedUserId();
        log.debug("현재 로그인한 유저 ID: {}", userId);

        //  학원 관계자인지 확인
        List<Long> ownedAcademyIds = getOwnedAcademyIds(userId);
        if (ownedAcademyIds.isEmpty()) {
            log.warn("학원 관계자가 아님 → 조회 불가: userId={}", userId);
            throw new CustomException(ReviewErrorCode.NOT_ACADEMY_MANAGER);
        }

        //  검증 완료 후 요청 실행
        req.setUserId(userId);
        List<AcademyLikedUsersDto> likedAcademies = mapper.getAllOwnedAcademyLikes(req);

        if (likedAcademies.isEmpty()) {
            userMessage.setMessage("소유한 학원에서 좋아요한 유저가 없습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("모든 학원의 좋아요한 유저 조회 완료.");
        return likedAcademies;
    }


    /**
     * 특정 유저가 좋아요한 학원 목록 조회
     */
    public List<LikedAcademyDto> getUserLikesWithPics(UserLikeGetListReq req) {
        // 🔥 userId가 null이면 JWT에서 가져오기
        if (req.getUserId() == null) {
            log.warn("userId가 null이므로 JWT에서 가져옵니다.");
            req.setUserId(getAuthenticatedUser().getSignedUserId());
        }

        try {
            List<LikedAcademyDto> likedAcademies = mapper.getUserLikesWithPics(req);
            if (likedAcademies.isEmpty()) {
                userMessage.setMessage("해당 유저가 좋아요 한 학원이 없습니다.");
                return Collections.emptyList();
            }

            userMessage.setMessage("좋아요 한 학원 조회 완료.");
            return likedAcademies;
        } catch (Exception e) {
            log.error("좋아요 학원 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            userMessage.setMessage("좋아요 학원 목록 조회 중 오류가 발생했습니다.");
            return Collections.emptyList();
        }
    }

    /**
     * 유저 존재 여부 확인
     */
    public int checkUserExists(long userId) {
        return userId > 0 ? mapper.checkUserExists(userId) : 0;
    }

    /**
     * 학원 존재 여부 확인
     */
    public int checkAcaExists(long acaId) {
        return acaId > 0 ? mapper.checkAcaExists(acaId) : 0;
    }

    /**  학원 관계자 권한 검증 */
    /**  학원 관계자 권한 검증 */
    private void checkUserAcademyOwnership(Long acaId, Long userId) {
        if (acaId == null || userId == null) {
            log.error("checkUserAcademyOwnership() - acaId 또는 userId가 null입니다: acaId={}, userId={}", acaId, userId);
            throw new CustomException(ReviewErrorCode.UNAUTHORIZED_ACADEMY_ACCESS);
        }

        if (!isUserLinkedToAcademy(acaId, userId)) {
            throw new CustomException(ReviewErrorCode.UNAUTHORIZED_ACADEMY_ACCESS);
        }
    }
    private boolean isUserLinkedToAcademy(Long acaId, Long userId) {
        if (acaId == null || userId == null) {
            log.warn("isUserLinkedToAcademy() - acaId 또는 userId가 null이므로 false 반환.");
            return false;
        }

        Integer result = mapper.isUserLinkedToAcademy(acaId, userId);
        return result != null && result > 0;
    }
}
package com.green.acamatch.like;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
@Slf4j
public class LikeController {
    private final LikeService service;
    private final UserMessage userMessage;


    /**
     * 학원 좋아요 등록
     * @param p AcaLikeReq 좋아요 등록 요청 (유저 ID, 학원 ID)
     * @return 좋아요 등록 결과 메시지와 유저 프로필 목록
     */
    @PostMapping
    @Operation(summary = "좋아요 등록", description = "특정 학원에 좋아요를 등록합니다.")
    public ResultResponse<AcaLikeRes> addLike(@RequestBody AcaLikeReq p) {
        log.debug("addLike 요청 데이터: userId={}, acaId={}", p.getUserId(), p.getAcaId());  // 로그 추가
        AcaLikeRes response = service.addLike(p);
        return ResultResponse.<AcaLikeRes>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(response)
                .build();
    }

    /**
     * 학원 좋아요 취소
     * @param p AcaDelLikeReq 좋아요 취소 요청 (유저 ID, 학원 ID)
     * @return 좋아요 취소 결과 메시지와 남은 유저 프로필 목록
     */
    @DeleteMapping
    @Operation(summary = "좋아요 취소", description = "특정 학원의 좋아요를 취소합니다.")
    public ResultResponse<AcaLikeRes> removeLike(@ParameterObject @ModelAttribute AcaDelLikeReq p) {
        AcaLikeRes response = service.removeLike(p);
        return ResultResponse.<AcaLikeRes>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(response)
                .build();
    }

    @GetMapping("/user")
    public ResultResponse<List<LikedAcademyDto>> getUserLikes(
            @RequestParam long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("getUserLikes 요청 데이터: userId={}, page={}, size={}", userId, page, size); // 로그 추가
        UserLikeGetListReq req = new UserLikeGetListReq(page, size);
        req.setUserId(userId);

        List<LikedAcademyDto> likedAcademies = service.getUserLikesWithPics(req);
        return ResultResponse.<List<LikedAcademyDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedAcademies)
                .build();
    }

    /**
     * 특정 학원을 좋아요한 유저들의 ID와 프로필 사진 조회
     */
    @GetMapping("/list")
    @Operation(summary = "좋아요 한 유저 목록 조회", description = "특정 학원을 좋아요한 유저들의 ID와 프로필 사진 목록을 조회합니다.")
    public ResultResponse<List<LikedUserDto>> getLikedUserPics(
            @RequestParam long acaId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        AcaLikedUserGetReq req = new AcaLikedUserGetReq(page, size);
        req.setAcaId(acaId); // acaId 설정
        List<LikedUserDto> likedUserPics = service.getLikedUserDetails(req);
        return ResultResponse.<List<LikedUserDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedUserPics)
                .build();
    }
}
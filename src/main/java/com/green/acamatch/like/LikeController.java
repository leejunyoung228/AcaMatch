package com.green.acamatch.like;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.like.dto.LikedAcademyDto;
import com.green.acamatch.like.dto.LikedUserDto;
import com.green.acamatch.like.model.AcaDelLikeReq;
import com.green.acamatch.like.model.AcaLikeReq;
import com.green.acamatch.like.model.AcaLikeRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
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
    public ResultResponse<AcaLikeRes> addLike(@ParameterObject @ModelAttribute AcaLikeReq p) {
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

    /**
     * 유저의 좋아요한 학원 목록 및 사진 조회
     * @param userId 유저 ID
     * @return 유저가 좋아요한 학원의 ID와 사진 리스트
     */
    @GetMapping
    @Operation(summary = "좋아요 한 학원 목록 및 사진 조회", description = "특정 유저가 좋아요한 학원의 목록 및 사진을 조회합니다.")
    public ResultResponse<List<LikedAcademyDto>> getUserLikes(@Parameter(description = "유저 ID") @RequestParam long userId) {
        List<LikedAcademyDto> likedAcademies = service.getUserLikesWithPics(userId);
        return ResultResponse.<List<LikedAcademyDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedAcademies)
                .build();
    }


    /**
     * 특정 학원을 좋아요한 유저들의 ID와 프로필 사진 조회
     * @param acaId 학원 ID
     * @return 좋아요한 유저들의 ID와 프로필 사진 리스트
     */
    @GetMapping("/list")
    @Operation(summary = "좋아요 한 유저 목록 조회", description = "특정 학원을 좋아요한 유저들의 ID와 프로필 사진 목록을 조회합니다.")
    public ResultResponse<List<LikedUserDto>> getLikedUserPics(@Parameter(description = "학원 ID") @RequestParam long acaId) {
        List<LikedUserDto> likedUserPics = service.getLikedUserDetails(acaId);
        return ResultResponse.<List<LikedUserDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedUserPics)
                .build();
    }
}
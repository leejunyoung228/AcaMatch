package com.green.acamatch.like;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.like.dto.*;
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
@Tag(name = "좋아요 관리")

public class LikeController {
    private final LikeService service;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "좋아요 등록", description = "특정 학원에 좋아요를 등록합니다.")
    public ResultResponse<AcaLikeRes> addLike(
            @RequestBody AcaLikeReq req) {
        log.debug("좋아요 등록 요청: userId={}, acaId={}", req.getUserId(), req.getAcaId());
        AcaLikeRes response = service.addLike(req);
        return ResultResponse.<AcaLikeRes>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(response)
                .build();
    }


    @DeleteMapping
    @Operation(summary = "좋아요 취소", description = "특정 학원의 좋아요를 취소합니다.")
    public ResultResponse<AcaLikeRes> removeLike(@RequestBody AcaDelLikeReq req) {
        log.debug("좋아요 취소 요청: userId={}, acaId={}", req.getUserId(), req.getAcaId());

        // 서비스에서 SecurityContextHolder를 통해 JWT 토큰 처리
        AcaLikeRes response = service.removeLike(req);
        return ResultResponse.<AcaLikeRes>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(response)
                .build();
    }
    @GetMapping("/user")
    @Operation(summary = "유저가 좋아요 한 학원 조회", description = "특정 유저가 좋아요 한 학원의 목록을 조회합니다.")
    public ResultResponse<List<LikedAcademyDto>> getUserLikes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("유저 좋아요 목록 조회 요청: page={}, size={}", page, size);
        UserLikeGetListReq req = new UserLikeGetListReq(page, size);
        List<LikedAcademyDto> likedAcademies = service.getUserLikesWithPics(req);
        return ResultResponse.<List<LikedAcademyDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedAcademies)
                .build();
    }


    @GetMapping("/all-owned-academy-likes")
    @Operation(summary = "소유한 모든 학원의 좋아요 유저 목록 조회", description = "학원 관계자가 소유한 모든 학원의 좋아요 유저 목록을 조회합니다.")
    public ResultResponse<List<AcademyLikedUsersDto>> getAllOwnedAcademyLikes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.debug("소유한 학원의 좋아요 유저 목록 조회 요청: page={}, size={}", page, size);

        // 🔥 로그인된 유저 가져오기 (JWT 사용)
        AcaLikedUserGetReq req = new AcaLikedUserGetReq(page, size);
        List<AcademyLikedUsersDto> likedAcademies = service.getAllOwnedAcademyLikes(req);


        return ResultResponse.<List<AcademyLikedUsersDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(likedAcademies)
                .build();
    }

}
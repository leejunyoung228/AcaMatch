package com.green.acamatch.user.contoller;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.user.model.RelationshipReq;
import com.green.acamatch.user.model.RelationshipRes;
import com.green.acamatch.user.service.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/relationship")
@Tag(name = "유저 관계 관리 API")
public class RelationshipController {
    private final RelationshipService relationshipService;

    @GetMapping("{student-id}")
    @Operation(summary = "요청 승인(부모 전용)", description = "부모가 자식한테서 온 관계 등록 요청 승인")
    public ResultResponse<Integer> acceptRelationship(@PathVariable("student-id") Long studentId) {
        int res = relationshipService.acceptRelationship(studentId);
        return ResultResponse.<Integer>builder()
                .resultMessage("관계 등록 성공")
                .resultData(res)
                .build();
    }

    @PostMapping
    @Operation(summary = "요청(자녀 전용)", description = "자식이 부모의 이메일로 관계 등록 요청")
    public ResultResponse<Integer> addRelationship(@RequestBody RelationshipReq req) {
        int res =  relationshipService.addRelationship(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("관계 등록 요청 성공")
                .resultData(res)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "요청 삭제(자녀 전용)", description = "자식이 부모의 이메일로 관계 등록 요청 취소</br>승인 전까지만 가능")
    public ResultResponse<Integer> deleteRelationshipRequest(@RequestBody RelationshipReq req) {
        int res =  relationshipService.deleteRelationshipRequest(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("관계 등록 요청 취소 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("list/{type}")
    @Operation(summary = "관계 조회(부모 자녀 둘 다 사용 가능)", description = "type : {1:부모 입장, 2:학생 입장}</br>updatedAt은 관계 등록이 완료되면 나옵니다")
    public ResultResponse<List<RelationshipRes>> getRelationships(@PathVariable int type) {
        List<RelationshipRes> res = relationshipService.getRelationships(type);
        return ResultResponse.<List<RelationshipRes>>builder()
                .resultMessage("관계 목록 조회 성공")
                .resultData(res)
                .build();
    }
}

package com.green.acamatch.board;

import com.green.acamatch.board.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공지사항 관리", description = "공지사항 등록, 가져오기, 수정, 삭제")
@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "공지사항 등록", description = "acaId / userId 하나만 입력하면 됩니다.")
    public ResultResponse<Integer> postBoard(@Valid @ParameterObject @ModelAttribute BoardPostReq p) {
        Integer res = boardService.postBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(res == 1 ? "공지사항 등록 완료" : "공지사항 등록 실패")
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "공지사항 상세보기", description = "acaId / userId 하나만 입력하면 됩니다.")
    public ResultResponse<List<BoardGetDetailDto>> getBoardDetail(@ModelAttribute @ParameterObject BoardGetDetailReq p) {
        List<BoardGetDetailDto> res = boardService.getBoardDetail(p);
        return ResultResponse.<List<BoardGetDetailDto>>builder()
                .resultMessage("공지사항 리스트 불러오기 완료")
                .resultData(res)
                .build();
    }

    @PutMapping
    @Operation(summary = "공지사항 수정", description = "acaId / userId 하나만 입력하면 됩니다.")
    public ResultResponse<Integer> updBoard(@Valid @ParameterObject @ModelAttribute BoardPutReq p) {
        Integer res = boardService.updBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(res == 1 ? "공지사항 수정 완료" : "공지사항 수정 실패")
                .resultData(res)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "공지사항 삭제", description = "acaId / userId 하나만 입력하면 됩니다.")
    public ResultResponse<Integer> delBoard(@Valid @ModelAttribute @ParameterObject BoardDelReq p){
        Integer res = boardService.delBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(res == 1 ? "공지사항 삭제 완료" : "공지사항 삭제 실패")
                .resultData(res)
                .build();
    }
}
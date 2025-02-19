package com.green.acamatch.board;

import com.green.acamatch.board.model.BoardPostReq;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항 관리", description = "공지사항 등록, 수정, 삭제")
@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "공지사항 등록")
    public ResultResponse<Integer> postBoard(@Valid @RequestBody BoardPostReq p) {
        Integer res = boardService.postBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("공지사항 등록 완료")
                .resultData(res)
                .build();
    }
}
package com.green.acamatch.board;

import com.green.acamatch.board.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;

@Tag(name = "공지사항 관리", description = "공지사항 등록, 수정, 삭제")
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
                .resultMessage("공지사항 등록 완료")
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "공지사항 리스트 불러오기")
    public ResultResponse<List<BoardGetDto>> getBoardList(@ModelAttribute @ParameterObject BoardGetDetailReq p) {
        List<BoardGetDto> res = boardService.getBoardList(p);
        return ResultResponse.<List<BoardGetDto>>builder()
                .resultMessage("공지사항 리스트 불러오기 완료")
                .resultData(res)
                .build();
    }

    @GetMapping("/detail")
    @Operation(summary = "공지사항 상세보기")
    public ResultResponse<List<BoardGetDetailRes>> getBoardDetail(@ModelAttribute @ParameterObject BoardGetDetailReq p) {
        List<BoardGetDetailRes> res = boardService.getBoardDetail(p);
        return ResultResponse.<List<BoardGetDetailRes>>builder()
                .resultMessage("공지사항 상세보기 완료")
                .resultData(res)
                .build();
    }

    @PutMapping
    @Operation(summary = "공지사항 수정")
    public ResultResponse<Integer> updBoard(@Valid @ParameterObject @ModelAttribute BoardPutReq p) {
        Integer res = boardService.updBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("공지사항 수정 완료")
                .resultData(res)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "공지사항 삭제")
    public ResultResponse<Integer> delBoard(@Valid @ModelAttribute @ParameterObject BoardDelReq p){
        Integer res = boardService.delBoard(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("공지사항 삭제 완료")
                .resultData(res)
                .build();
    }
}
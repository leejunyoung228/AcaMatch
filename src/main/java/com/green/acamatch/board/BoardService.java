package com.green.acamatch.board;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.board.model.*;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.board.Board;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final BoardRepository boardRepository;
    private final AcademyRepository academyRepository;
    private final UserRepository userRepository;

    public int postBoard(BoardPostReq p) {
        try {
            Board board = new Board();
            if (p.getUserId() != null && p.getAcaId() == null) {
                User user = userRepository.findById(p.getUserId())
                        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
                board.setUser(user);
            } else if (p.getAcaId() != null && p.getUserId() == null) {
                Academy academy = academyRepository.findById(p.getAcaId())
                        .orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
                board.setAcademy(academy);
            } else throw new CustomException(CommonErrorCode.INVALID_PARAMETER);

            board.setBoardName(p.getBoardName());
            board.setBoardComment(p.getBoardComment());
            board.setCreatedAt(LocalDateTime.now());

            boardRepository.save(board);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    public List<BoardGetListDto> getBoardList(BoardGetListReq p) {
        List<BoardGetListDto> result = boardMapper.getBoardList(p);
        return result;
    }

    public BoardGetDetailRes getBoardDetail(BoardGetDetailReq p) {
        BoardGetDetailRes result = boardMapper.getBoardDetail(p);
        return result;
    }

    public int updBoard(BoardPutReq p) {
        try {
            Board board = boardRepository.findById(p.getBoardId()) // 기존 데이터 조회
                    .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));

            if (p.getUserId() != null && p.getAcaId() == null) {
                User user = userRepository.findById(p.getUserId())
                        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
                board.setUser(user);
            } else if (p.getAcaId() != null && p.getUserId() == null) {
                Academy academy = academyRepository.findById(p.getAcaId())
                        .orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
                board.setAcademy(academy);
            }

            if (!StringUtils.hasText(p.getBoardName())) {
                throw new CustomException(BoardErrorCode.FAIL_TO_UPD);
            }
            if (!StringUtils.hasText(p.getBoardComment())) {
                throw new CustomException(BoardErrorCode.FAIL_TO_UPD);
            }

            board.setBoardName(p.getBoardName());
            board.setBoardComment(p.getBoardComment());

            boardRepository.save(board); // 업데이트 수행

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    public int delBoard(BoardDelReq p) {
        try {
            Board board = boardRepository.findById(p.getBoardId()) // 기존 데이터 조회
                    .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));

            if (p.getUserId() != null && p.getAcaId() == null) {
                User user = userRepository.findById(p.getUserId())
                        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
                board.setUser(user);
            } else if (p.getAcaId() != null && p.getUserId() == null) {
                Academy academy = academyRepository.findById(p.getAcaId())
                        .orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
                board.setAcademy(academy);
            } else throw new CustomException(BoardErrorCode.FAIL_TO_DEL);

            boardRepository.delete(board);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}
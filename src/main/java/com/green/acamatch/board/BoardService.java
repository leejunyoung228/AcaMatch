package com.green.acamatch.board;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.board.model.BoardDelReq;
import com.green.acamatch.board.model.BoardPostReq;
import com.green.acamatch.board.model.BoardPutReq;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.board.Board;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final AuthenticationFacade authenticationFacade;
    private final BoardRepository boardRepository;
    private final AcademyRepository academyRepository;
    private final UserRepository userRepository;

    public int postBoard(BoardPostReq p) {
        User user = userRepository.findById(authenticationFacade.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Academy academy = academyRepository.findById(p.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));

        Board board = new Board();
        board.setUser(user);
        board.setAcademy(academy);
        board.setBoardName(p.getBoardName());
        board.setBoardComment(p.getBoardComment());
        board.setCreatedAt(p.getCreatedAt());

        int result = boardMapper.insBoard(p);
        boardRepository.save(board);

        return result;
    }

    public int updBoard(BoardPutReq p) {
        User user = userRepository.findById(authenticationFacade.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Board board = boardRepository.findById(p.getBoardId()).orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));

        if(!StringUtils.hasText(p.getBoardName())) {
            throw new CustomException(BoardErrorCode.FAIL_TO_UPD);
        }
        if(!StringUtils.hasText(p.getBoardComment())) {
            throw new CustomException(BoardErrorCode.FAIL_TO_UPD);
        }

        user.setUserId(authenticationFacade.getUserId());
        board.setBoardName(p.getBoardName());
        board.setBoardComment(p.getBoardComment());

        return boardMapper.updBoard(p);
    }

    public int delBoard(BoardDelReq p) {
        Board board = boardRepository.findById(p.getBoardId()).orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));
        if(board.getUser().getUserId() != authenticationFacade.getUserId()) {
            throw new CustomException(BoardErrorCode.FAIL_TO_DEL);
        }

        int result = boardMapper.delBoard(p);
        boardRepository.delete(board);
        return result;
    }
}
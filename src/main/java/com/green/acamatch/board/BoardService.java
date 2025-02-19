package com.green.acamatch.board;

import com.green.acamatch.board.model.BoardPostReq;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.board.Board;
import com.green.acamatch.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final AuthenticationFacade authenticationFacade;
    private final BoardRepository boardRepository;

    public int postBoard(BoardPostReq p) {
        User user = new User();
        user.setUserId(authenticationFacade.getUserId());

        Academy academy = new Academy();
        academy.setAcaId(p.getAcaId());

        Board board = new Board();
        board.setUser(user);
        board.setAcademy(academy);
        board.setBoardName(p.getBoardName());
        board.setBoardComment(p.getBoardComment());
        board.setCreatedAt(p.getCreatedAt());

        boardRepository.save(board);

        return boardMapper.insBoard(p);
    }
}

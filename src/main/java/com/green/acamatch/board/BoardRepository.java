package com.green.acamatch.board;

import com.green.acamatch.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
}

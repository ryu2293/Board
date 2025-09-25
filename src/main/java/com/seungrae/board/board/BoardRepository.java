package com.seungrae.board.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMembers_Id(Long memberId);
    Optional<Board> findByIdAndMembers_Id(Long boardId, Long memberId);

}

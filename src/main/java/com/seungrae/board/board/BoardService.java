package com.seungrae.board.board;

import com.seungrae.board.colum.BoardColumn;
import com.seungrae.board.colum.BoardColumnRP;
import com.seungrae.board.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardColumnRP boardColumnRP;

    @Transactional
    public void createBoard(String title, Long memberId){
        var member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board = new Board();
        board.setTitle(title);
        board.getMembers().add(member);

        String[] st = {"To do", "In Progress", "Done"};
        for(int i=0; i<3; i++){
            BoardColumn boardColumn = new BoardColumn();
            boardColumn.setTitle(st[i]);
            boardColumn.setColumnOrder(i+1);
            boardColumn.setBoard(board);

            board.getColumns().add(boardColumn);
        }

        boardRepository.save(board);
    }

    @Transactional
    public List<Board> getMyBoard(Long memberId){
        return boardRepository.findByMembers_Id(memberId);
    }

    @Transactional
    public Board checkBoard(Long boardId, Long memberId){
        return boardRepository.findByIdAndMembers_Id(boardId, memberId).orElseThrow(() -> new IllegalArgumentException("권한이 없습니다!"));
    }
}

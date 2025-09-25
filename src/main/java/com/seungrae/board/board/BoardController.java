package com.seungrae.board.board;

import com.seungrae.board.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("/board")
    public String newBoard(){
        return "board.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/list")
    public String test(Authentication auth, Model model){
        var user = (CustomUser) auth.getPrincipal();
        var boards = boardRepository.findByMembers_Id(user.id);
        model.addAttribute("boards", boards);
        return "boardList.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/{boardId}")
    public String board(
            @PathVariable Long boardId,
            Model model,
            Authentication auth
    ){
        CustomUser user = (CustomUser) auth.getPrincipal();

        try{
            var board = boardService.checkBoard(boardId, user.id);
            model.addAttribute("board", board);
            return "boardPage.html";
        } catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/error";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/boardAct")
    public String createBoard(Authentication auth, String title, Model model){
        if(title.isEmpty() || title == null) {
            model.addAttribute("error", "제목을 입력하세요.");
            return "board.html";
        }
        CustomUser user = (CustomUser) auth.getPrincipal();
        boardService.createBoard(title, user.id);

        return "index.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/boardList")
    @ResponseBody
    public List<Board> boardList(Authentication auth){
        CustomUser user = (CustomUser) auth.getPrincipal();
        return boardService.getMyBoard(user.id);
    }
}

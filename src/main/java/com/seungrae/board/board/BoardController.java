package com.seungrae.board.board;

import com.seungrae.board.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public String boardPage(){
        return "board.html";
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

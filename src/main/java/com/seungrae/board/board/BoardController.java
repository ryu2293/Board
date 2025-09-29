package com.seungrae.board.board;

import com.seungrae.board.CustomUser;
import com.seungrae.board.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PostMapping("/board/{id}/invite")
    public String inviteMember(@PathVariable Long id, String email, RedirectAttributes redirectAttributes){
        try{
            if(email == null || email.isEmpty()){
                redirectAttributes.addFlashAttribute("msg", "빈칸을 입력하세요.");
                return "redirect:/board/"+ id;
            }
            boardService.invite(id, email);
            redirectAttributes.addFlashAttribute("msg", "성공적으로 초대하였습니다.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }
            return "redirect:/board/"+ id;
    }

}

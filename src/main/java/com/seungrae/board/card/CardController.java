package com.seungrae.board.card;

import com.seungrae.board.dto.CardMoveDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardRepository cardRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/column/{columnId}/cards")
    public String createCard(
            @PathVariable Long columnId,
            String title,
            String description
    ){
        Card newCard = cardService.cardCreate(title, description, columnId);
        Long boardId = newCard.getBoardColumn().getBoard().getId();
        return "redirect:/board/" + boardId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/card/{cardId}/update")
    public String updateCard(@PathVariable Long cardId, String title, String description){
        Card card = cardService.cardUpdate(title, description, cardId);
        var boardId = card.getBoardColumn().getBoard().getId();
        return "redirect:/board/"+boardId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/card/{cardId}/delete")
    public String deleteCard(@PathVariable Long cardId, RedirectAttributes redirectAttributes){
        try{
            Card card = cardService.cardDelete(cardId);
            Long boardId = card.getBoardColumn().getBoard().getId();
            redirectAttributes.addFlashAttribute("message", "카드가 삭제되었습니다.");
            return "redirect:/board/" + boardId;
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/board/list";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<String> moveCard(
            @PathVariable Long cardId,
            @Valid @RequestBody CardMoveDto req,
            BindingResult bindingResult
    ){
        try{
            if(bindingResult.hasErrors()){
                String error = bindingResult.getFieldError().getDefaultMessage();
                return ResponseEntity.status(401).body(error);
            }
            cardService.cardMove(cardId, req.newColumnId(), req.newOrder());
            return ResponseEntity.ok().body("카드 이동에 성공하였습니다.");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

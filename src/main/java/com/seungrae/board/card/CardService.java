package com.seungrae.board.card;

import com.seungrae.board.colum.BoardColumnRP;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final BoardColumnRP boardColumnRP;
    private final CardRepository cardRepository;

    @Transactional
    public Card cardCreate(String title, String description, Long columnId){
        var boardColumn = boardColumnRP.findById(columnId).orElseThrow(()-> new IllegalArgumentException("해당 컬럼을 찾을 수 없습니다."));

        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setBoardColumn(boardColumn);

        int order = card.getBoardColumn().getCards().size()+1;
        card.setCardOrder(order);
        return cardRepository.save(card);
    }

    @Transactional
    public Card cardUpdate(String title, String description, Long cardId){
       var card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("해당 카드가 없습니다."));
       card.setTitle(title);
       card.setDescription(description);
       return cardRepository.save(card);
    }

    @Transactional
    public Card cardDelete(Long cardId){
        var card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("해당 카드가 없습니다."));
        cardRepository.delete(card);
        return card;
    }
}

package com.seungrae.board;


import com.seungrae.board.dto.CardEditingDto;
import com.seungrae.board.dto.InviteEditingDto;
import com.seungrae.board.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/board/{boardId}/editing")
    public void handleEditingStatus(
            @DestinationVariable Long boardId,
            CardEditingDto req
    ){
        var messageType = req.isEditing() ? WebSocketMessage.MessageType.CARD_EDITING_STARTED :
                WebSocketMessage.MessageType.CARD_EDITING_ENDED;
        var message = new WebSocketMessage(messageType, req);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId, message);
    }

    @MessageMapping("/board/{boardId}/invite")
    public void handleInviteStatus(
            @DestinationVariable Long boardId,
            InviteEditingDto req
    ){
        var messageType = req.isTyping() ?
                WebSocketMessage.MessageType.INVITE_STARTED :
                WebSocketMessage.MessageType.INVITE_ENDED;
        var message = new WebSocketMessage(messageType, req);
        simpMessagingTemplate.convertAndSend("/topic/board/"+boardId, message);
    }
}

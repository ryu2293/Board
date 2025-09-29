package com.seungrae.board.dto;

import java.awt.*;

public record WebSocketMessage(
        MessageType type, // 종류
        Object payload // 데이터
) {
    public enum MessageType {
        CARD_CREATED,
        CARD_UPDATED,
        CARD_DELETED,
        CARD_MOVED
    }
}

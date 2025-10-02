package com.seungrae.board.dto;

public record CardEditingDto(
        Long cardId,
        String username,
        boolean isEditing
) {
}

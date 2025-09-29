package com.seungrae.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardMoveDto(
        @NotNull(message = "오류가 발생했습니다.")
        Long newColumnId,
        @NotNull(message = "오류가 발생했습니다.")
        Integer newOrder
) {
}

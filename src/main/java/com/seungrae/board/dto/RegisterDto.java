package com.seungrae.board.dto;

import jakarta.validation.constraints.*;

public record RegisterDto(
        @NotBlank @Email
        String email,
        @NotBlank @Size(min = 4, max = 10, message = "아이디는 4~10자로 작성해주세요.")
        String username,
        @NotBlank @Size(min = 6, max = 15, message = "비밀번호는 6~15자로 작성해주세요.")
        @Pattern(
                regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",
                message = "비밀번호는 영문자, 숫자, 특수기호(@$!%*#?&)를 포함해야 합니다."
        )
        String password,
        @NotBlank
        String passwordCh
) {
    @AssertTrue(message = "비밀번호를 확인하세요.")
    public boolean isCheckPw(){
        return password != null && password.equals(passwordCh);
    }
}

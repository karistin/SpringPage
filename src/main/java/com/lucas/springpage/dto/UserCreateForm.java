package com.lucas.springpage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 회원가입 Form
@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "회원가입 아이디는 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 사항입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호는 필수 사항입니다.")
    private String password2;
    // 입력한 비밀번호 확인
    @NotEmpty(message = "이메일은 필수사항입니다. ")
    @Email
    private String email;
}

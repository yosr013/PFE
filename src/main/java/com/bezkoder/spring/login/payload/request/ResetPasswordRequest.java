package com.bezkoder.spring.login.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data

public class ResetPasswordRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;
  
    @NotBlank
    @Size(min = 6, max = 40)
    private String newConfirmation;
}

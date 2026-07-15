package com.backend.cafe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ForgotPasswordRequest {

    private String username;
    private String mobileNumber;
    private String newPassword;
    private String confirmPassword;
}

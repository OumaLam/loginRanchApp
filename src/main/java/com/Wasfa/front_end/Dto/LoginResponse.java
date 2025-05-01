package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private String message;
    private String fonction;
    private String token;

    public LoginResponse(String message, String fonction) {
        this.message = message;
        this.fonction = fonction;
    }

}


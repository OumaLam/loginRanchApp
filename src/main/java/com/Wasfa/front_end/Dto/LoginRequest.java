package com.Wasfa.front_end.Dto;

import lombok.Data;

@Data
public class LoginRequest {
        private String email;
        private String motDePasse;
        private String deviceId;
        private String fonction;
}


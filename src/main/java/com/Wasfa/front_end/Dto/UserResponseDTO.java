package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor

    public class UserResponseDTO {
        private String email;
        private String nom;
        private String prenom;
        private String role;
}

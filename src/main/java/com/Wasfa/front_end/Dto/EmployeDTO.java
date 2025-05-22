package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeDTO {
    private String cin;
    private String nom;
    private String prenom;
    private String role;
    private LocalDate dateEmbauche;
    private String typeContrat;
}

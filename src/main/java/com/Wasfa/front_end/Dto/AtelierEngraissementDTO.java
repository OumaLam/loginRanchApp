package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtelierEngraissementDTO {
    private int idAtelierEngraissement;
    private LocalDate dateDebutAE;
    private LocalDate dateFinAE;
    private double objectifPoids;
    private EmployeDTO chefAtelier;
}

package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuiviReproductionDTO {
    private int idSuiviReproduction;
    private LocalDate dateSaillie;
    private String maleIdentifiant;
    private Boolean gestationConfirmee;
    private LocalDate dateNaissance;
    private int nbPetit;
}

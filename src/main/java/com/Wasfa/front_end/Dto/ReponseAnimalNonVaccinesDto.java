package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class ReponseAnimalNonVaccinesDto {
    private String idAnimal;
    private String race;
    private String sexe;
    private LocalDate dateNaissance;
}

package com.Wasfa.front_end.Dto;

import com.Wasfa.front_end.Entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatutAnimalDTO {
    private Long id;
    private LocalDate dateStatut;
    private String statutTitre;
    private String  prix;
    private String cause;
    private Animal animal;
}

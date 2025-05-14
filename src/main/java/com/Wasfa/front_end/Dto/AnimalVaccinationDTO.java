package com.Wasfa.front_end.Dto;

import com.Wasfa.front_end.Entity.AnimalVaccination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalVaccinationDTO {
    private List<String> animalIds;
    private String idAnimal;
    private LocalDate dateVaccination;
    private int idVaccin;

    public AnimalVaccinationDTO(String idAnimal,
                             LocalDate dateVaccination,
                             int idVaccination) {
        this.idAnimal = idAnimal;
        this.dateVaccination = dateVaccination;
        this.idVaccin = idVaccination;
    }


}

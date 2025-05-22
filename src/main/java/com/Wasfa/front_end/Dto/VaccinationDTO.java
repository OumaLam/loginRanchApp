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
public class VaccinationDTO {
    private int idVaccin;
    private String vaccinNom;
    private String cibleAge;
    private String cibleSexe;
    private String remarqueVaccination;
    private LocalDate dateVaccination;


}

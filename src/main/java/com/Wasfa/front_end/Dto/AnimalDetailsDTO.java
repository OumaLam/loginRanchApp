package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
public class AnimalDetailsDTO {
    private AnimalDTO animal;
    private List<ControlMedicalDTO> controles;
    private List<VaccinationDTO> vaccinations;
    private List<HistoriquePoidsDTO> poids;
}

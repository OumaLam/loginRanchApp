package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {
    private String idAnimal;
    private LocalDate dateNaissance;
    private String race;
    private String sexe;
    private String statutTitre;
    private String prix;
    private String cause;
    private StatutAnimalDTO statut;
    private List<ControlMedicalDTO> controles;
    private List<VaccinationDTO> vaccinations;
    private List<HistoriquePoidsDTO> poids;
    private List<SuiviReproductionDTO> reproductions;
    private AtelierEngraissementDTO atelier;
}

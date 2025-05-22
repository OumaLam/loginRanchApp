package com.Wasfa.front_end.Dto;

import com.Wasfa.front_end.Entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlMedicalDTO {
    private int idControle;
    private LocalDate dateControl;
    private String maladie;
    private String traitement;
    private String idAnimal;

    public ControlMedicalDTO(int idControle, LocalDate dateControl, String maladie, String traitement) {
        this.idControle = idControle;
        this.dateControl = dateControl;
        this.maladie = maladie;
        this.traitement = traitement;
    }

}

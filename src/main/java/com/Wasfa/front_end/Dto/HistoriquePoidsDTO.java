package com.Wasfa.front_end.Dto;

import com.Wasfa.front_end.Entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriquePoidsDTO {
    private int idPoid;
    private LocalDate dateMesure;
    private String poids;
    private Animal animal;

    public HistoriquePoidsDTO(int idPoid, LocalDate dateMesure, String poids) {
        this.idPoid=idPoid;
        this.dateMesure = dateMesure;
        this.poids = poids;
    }
}

package com.Wasfa.front_end.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suivi_reproduction")
public class SuiviReproduction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSuiviReproduction;

    private LocalDate dateSaillie;
    private String maleIdentifiant;
    private Boolean gestationConfirmee;
    private LocalDate dateNaissance;
    private int nbPetit;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
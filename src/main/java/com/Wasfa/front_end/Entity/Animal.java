package com.Wasfa.front_end.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animal")
public class Animal implements Serializable {
    @Id
    private String idAnimal;
    private LocalDate dateNaissance;
    private String race;
    private String sexe;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL)
    private StatutAnimal statut;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<ControlMedical> controles;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<AnimalVaccination> animalVaccinations;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<HistoriquePoids> poids;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<SuiviReproduction> reproductions;

    @ManyToOne
    @JoinColumn(name = "atelier_id")
    private AtelierEngraissement atelier;
}
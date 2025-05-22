package com.Wasfa.front_end.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historique_poids")
public class HistoriquePoids implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPoid;

    private LocalDate dateMesure;
    private String poids;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
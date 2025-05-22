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
@Table(name = "atelier_engraissement")
public class AtelierEngraissement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAtelierEngraissement;

    private LocalDate dateDebutAE;
    private LocalDate dateFinAE;
    private double objectifPoids;

    @ManyToOne
    @JoinColumn(name = "chef_atelier_cin")
    private EmployeRanch chefAtelier;

    @OneToMany(mappedBy = "atelier")
    private List<Animal> animaux;
}
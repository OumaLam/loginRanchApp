package com.Wasfa.front_end.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccination")
public class Vaccination implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVaccin;
    private String vaccinNom;
    private String cibleAge;
    private String cibleSexe;
    private String remarqueVaccination;

    @OneToMany(mappedBy = "vaccination", cascade = CascadeType.ALL)
    private List<AnimalVaccination> animalVaccinations;


}
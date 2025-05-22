package com.Wasfa.front_end.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "absence")
public class Absence implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbsence;

    private LocalDate dateDebutAbsence;
    private LocalDate dateFinAbsence;
    private String raisonAbsence;

    @ManyToOne
    @JoinColumn(name = "employe_cin")
    private EmployeRanch employe;
}
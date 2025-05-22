package com.Wasfa.front_end.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "control_medical")
public class ControlMedical implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idControle;

    private LocalDate dateControl;
    private String maladie;
    private String traitement;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
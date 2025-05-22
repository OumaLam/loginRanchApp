package com.Wasfa.front_end.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "statut_animal")
public class StatutAnimal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatut;

    private LocalDate dateStatut;
    private String statutTitre;//nee ,vendu ,acheter,abbatu,mort
    private String  prix;
    private String cause;

      @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
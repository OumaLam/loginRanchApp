    package com.Wasfa.front_end.Entity;

    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table (name = "employes_ranch2")
    public class EmployeRanch {

        @Id
        @Column(unique = true)
        private String cin;
        @Column(unique = true)
        private  String email;

        private String nom;

        private String prenom;

        private String fonction;

        private Double salaire;

        private LocalDate dateEmbauche;

        @Column(unique = true)
        private String cinHash;


        // Remplace "typeContrat" par "contratPdf" si tu utilises un fichier PDF
        @Column(name = "contrat_pdf")
        private String contratPdf;

        private boolean actif = true;
        @OneToOne(mappedBy = "employeRanch", cascade = CascadeType.ALL)
        @JsonManagedReference

        private Role roleEmploye;
    }

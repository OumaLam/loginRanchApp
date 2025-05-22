package com.Wasfa.front_end.Dto;

import com.Wasfa.front_end.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceDTO {
    private Long id;
    private LocalDate dateDebutAbsence;
    private LocalDate dateFinAbsence;
    private String raisonAbsence;
    private Role employe;
}

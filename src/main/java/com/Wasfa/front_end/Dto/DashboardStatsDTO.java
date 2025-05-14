package com.Wasfa.front_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalAnimaux;
    private long totalEmployes;
    private Map<String, Long> repartitionParStatut;
    private Map<String, Long> repartitionParSexe;
}


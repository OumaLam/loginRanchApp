package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Dto.DashboardStatsDTO;
import com.Wasfa.front_end.Dto.StatutDistributionDTO;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeRepository employeRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalAnimaux(animalRepository.count());
        stats.setTotalEmployes(employeRepository.count());

        // Répartition par statut
        Map<String, Long> statuts = animalRepository.countByStatutTitre();
        stats.setRepartitionParStatut(statuts);

//        // Répartition par sexe
//        Map<String, Long> sexes = animalRepository.countBySexe();
//        stats.setRepartitionParSexe(sexes);

        return stats;
    }

    public List<StatutDistributionDTO> getStatutDistribution() {
        return animalRepository.countByStatut();
    }
    public List<Map<String, Object>> getMonthlyCountByStatut(String statutTitre) {
        List<Object[]> results = animalRepository.countByMonthAndStatut(statutTitre);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("mois", row[0]);
            entry.put("count", row[1]);
            response.add(entry);
        }

        return response;
    }
    public long countEmployer() {
        return employeRepository.count();
    }

}


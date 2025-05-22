package com.Wasfa.front_end.Controllers;


import com.Wasfa.front_end.Dto.StatutDistributionDTO;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.services.AnimalService;
import com.Wasfa.front_end.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

     @RestController
     @RequestMapping("/api/dashboard")
     @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINAIRE')")
     @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public class DashboardController {

        @Autowired
        private AnimalService animalService;
        @Autowired
        private AnimalRepository animalRepository;
        @GetMapping("/stats/totalAnimaux")
        public long getStats() {
            long totalAnimaux = animalService.countAnimals();
            return totalAnimaux;
        }
        @GetMapping("/stats/totalEmployer")
        public long getStatEmployer() {
            long totalEmployer = dashboardService.countEmployer();
            return totalEmployer;
        }

    @GetMapping("/sexe-distribution")
    public Map<String, Long> getSexeDistribution() {
        Map<String, Long> map = new HashMap<>();
        map.put("male", animalRepository.countBySexe("male"));
        map.put("femelle", animalRepository.countBySexe("femelle"));
        return map;
    }


    @Autowired
    private DashboardService dashboardService;

//    @GetMapping("/stats")
//    public ResponseEntity<DashboardStatsDTO> getGlobalStats() {
//        return ResponseEntity.ok(dashboardService.getStats());
//    }
@GetMapping("/statut-distribution")
public List<StatutDistributionDTO> getStatutDistribution() {
    return dashboardService.getStatutDistribution();
}

    @GetMapping("statut-monthly/{statutTitre}")
    public List<Map<String, Object>> getMonthlyCountByStatut(@PathVariable String statutTitre) {
        return dashboardService.getMonthlyCountByStatut(statutTitre);
    }


}


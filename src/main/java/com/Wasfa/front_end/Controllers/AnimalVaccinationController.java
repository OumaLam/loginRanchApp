package com.Wasfa.front_end.Controllers;

import com.Wasfa.front_end.Dto.AnimalVaccinationDTO;
import com.Wasfa.front_end.Dto.ReponseAnimalNonVaccinesDto;
import com.Wasfa.front_end.Entity.Animal;
import com.Wasfa.front_end.Entity.AnimalVaccination;
import com.Wasfa.front_end.Entity.Vaccination;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.AnimalVaccinationRepository;
import com.Wasfa.front_end.repository.VaccinationRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AnimalVaccinationController {

    private final VaccinationRepository vaccinationRepository;
    private final AnimalRepository animalRepository;
    private final AnimalVaccinationRepository animalVaccinationRepository;

    public AnimalVaccinationController(VaccinationRepository vaccinationRepository,
                                 AnimalRepository animalRepository,
                                 AnimalVaccinationRepository animalVaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.animalRepository = animalRepository;
        this.animalVaccinationRepository = animalVaccinationRepository;
    }

    // ➤ Récupérer les animaux non vaccinés pour un vaccin
    @GetMapping("/animaux/non-vaccines")
    public List<ReponseAnimalNonVaccinesDto> getAnimauxNonVaccines(@RequestParam int vaccinId) {
        Vaccination vaccin = vaccinationRepository.findById(vaccinId)
                .orElseThrow(() -> new RuntimeException("Vaccination introuvable"));

        String[] ageParts = vaccin.getCibleAge().split("_");
        int annees = Integer.parseInt(ageParts[0]);
        int mois = Integer.parseInt(ageParts[1]);
        int jours = Integer.parseInt(ageParts[2]);

        LocalDate dateLimiteNaissance = LocalDate.now().minusYears(annees).minusMonths(mois).minusDays(jours);

        List<Animal> animaux = animalRepository.findAnimauxEligiblesNonVaccines(
                dateLimiteNaissance,
                vaccin.getCibleSexe(),
                (long) vaccinId
        );

        return animaux.stream()
                .map(a -> new ReponseAnimalNonVaccinesDto(a.getIdAnimal(), a.getRace(), a.getSexe(), a.getDateNaissance()))
                .collect(Collectors.toList());
    }


    // ➤ Enregistrer la vaccination pour plusieurs animaux
    @PostMapping("/animal-vaccinations/{vaccinationId}")
    public String enregistrerVaccinations(@RequestBody AnimalVaccinationDTO dto, @PathVariable Integer vaccinationId) {
        // Récupérer la vaccination correspondant à l'ID
        Vaccination vaccin = vaccinationRepository.findById(vaccinationId)
                .orElseThrow(() -> new RuntimeException("Vaccination non trouvée"));

        // Récupérer les animaux avec les IDs du DTO
        List<Animal> animaux = animalRepository.findAllById(dto.getAnimalIds());

        if (animaux == null || animaux.isEmpty()) {
            throw new IllegalArgumentException("Aucun animal trouvé pour les IDs spécifiés");
        }

        // Créer les enregistrements AnimalVaccination
        for (Animal animal : animaux) {
            AnimalVaccination av = new AnimalVaccination();
            av.setAnimal(animal);
            av.setVaccination(vaccin);
            av.setDateVaccination(LocalDate.now());
            animalVaccinationRepository.save(av);
        }

        return "Vaccination enregistrée pour " + animaux.size() + " animal(s)";
    }



}

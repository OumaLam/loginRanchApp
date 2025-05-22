package com.Wasfa.front_end.Controllers;

import com.Wasfa.front_end.Dto.*;
import com.Wasfa.front_end.Entity.ControlMedical;
import com.Wasfa.front_end.services.AnimalService;
import com.Wasfa.front_end.services.ControleMedicalService;
import com.Wasfa.front_end.services.PoidsService;
import com.Wasfa.front_end.services.VaccinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
@PreAuthorize("hasRole('ADMIN') or hasRole('VETERINAIRE')")
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private  PoidsService poidsService;
    @Autowired
    private VaccinationService VaccinationService;
    @Autowired
    private ControleMedicalService control;
    // GET : R�cup�rer la liste de tous les animaux
    @GetMapping
    public List<AnimalDTO> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    // GET : R�cup�rer un animal par son identification
    @GetMapping("/{id}")
    public AnimalDTO getAnimalById(@PathVariable String id) {
        return animalService.getAnimalById(id);
    }

    // POST : Ajouter un nouvel animal
    @PostMapping("/new")
    public AnimalDTO addAnimal(@RequestBody AnimalDTO animalDTO) {
        return animalService.addAnimal(animalDTO);
    }

    // PUT : Mettre a jour les informations d'un animal
    @PutMapping("/update")
    public ResponseEntity<AnimalDTO> updateAnimal(@RequestBody AnimalDTO dto) {
        AnimalDTO updated = animalService.updateAnimal(dto);
        return ResponseEntity.ok(updated);
    }


    // DELETE : Supprimer un animal
    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable String id) {
        animalService.deleteAnimal(id);
    }

    @GetMapping("/last")
    public List<AnimalDTO> getLastAnimals() {
        return animalService.getLastAnimals();
    }


    @GetMapping("/{id}/details")
    public AnimalDetailsDTO getAnimalDetails(
            @PathVariable String id,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return animalService.getAnimalDetails(id, type, date);
    }

    @PostMapping("/{id}/poids")
    public ResponseEntity<String> ajouterPoids(@PathVariable String id, @RequestBody HistoriquePoidsDTO dto) {
        poidsService.ajouterPoids(id, dto);
        return ResponseEntity.ok("Poids ajouté avec succès !");
    }
    @PostMapping("/{idAnimal}/control")
    public ResponseEntity<ControlMedical> ajouterControle(@PathVariable String idAnimal,@RequestBody ControlMedicalDTO dto) {
        return ResponseEntity.ok(control.addControlMedicalFromDTO(dto, idAnimal));
    }
    @PostMapping("/{id}/vaccination")
    public ResponseEntity<String> ajouterVaccination(@PathVariable String id, @RequestBody AnimalVaccinationDTO dto) {
        VaccinationService.ajouterVaccination(id,dto);
        return ResponseEntity.ok("vaccination ajouté avec succès !");
    }


}

package com.Wasfa.front_end.Controllers;

import com.Wasfa.front_end.Dto.VaccinationDTO;
import com.Wasfa.front_end.services.VaccinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/vaccinations")
@PreAuthorize("hasRole('ADMIN') or hasRole('VETERINAIRE')")

public class VaccinationController {

    @Autowired
    private VaccinationService vaccinationService;

    // GET : R�cup�rer la liste de toutes les vaccinations
    @GetMapping
    public List<VaccinationDTO> getAllVaccinations() {
        return vaccinationService.getAllVaccinations();
    }
    // GET : R�cup�rer la liste de toutes les vaccinations non injecter pour un animal
    @GetMapping("/nonInjecter/{idAnimal}")
    public List<VaccinationDTO> getAllVaccinationsNonInjecterPourAnimal(@PathVariable String idAnimal) {
        return vaccinationService.getAllVaccinationsNonInjecterPourAnimal(idAnimal);
    }

    // GET : R�cup�rer une vaccination par son ID
    @GetMapping("/{id}")
    public VaccinationDTO getVaccinationById(@PathVariable int id) {
        return vaccinationService.getVaccinationById(id);
    }

    // POST : Ajouter une nouvelle vaccination
    @PostMapping("/new")
    public VaccinationDTO addVaccination(@RequestBody VaccinationDTO vaccinationDTO) {
        return vaccinationService.addVaccination(vaccinationDTO);
    }

    // PUT : Mettre � jour les informations d'une vaccination
    @PutMapping("/{id}")
    public VaccinationDTO updateVaccination(@PathVariable int id, @RequestBody VaccinationDTO vaccinationDTO) {
        return vaccinationService.updateVaccination(id, vaccinationDTO);
    }

    // DELETE : Supprimer une vaccination
    @DeleteMapping("/{id}")
    public void deleteVaccination(@PathVariable int id) {
        vaccinationService.deleteVaccination(id);
    }
}

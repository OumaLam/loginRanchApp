package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Dto.AnimalVaccinationDTO;
import com.Wasfa.front_end.Dto.HistoriquePoidsDTO;
import com.Wasfa.front_end.Dto.VaccinationDTO;
import com.Wasfa.front_end.Entity.Animal;
import com.Wasfa.front_end.Entity.AnimalVaccination;
import com.Wasfa.front_end.Entity.HistoriquePoids;
import com.Wasfa.front_end.Entity.Vaccination;

import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.AnimalVaccinationRepository;
import com.Wasfa.front_end.repository.VaccinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VaccinationService {

    @Autowired
    private VaccinationRepository vaccinationRepository;
    @Autowired
    private  AnimalRepository animalRepository;
    @Autowired
    private AnimalVaccinationRepository animalVaccinRepository;
    public List<VaccinationDTO> getAllVaccinations() {
        return vaccinationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<VaccinationDTO> getAllVaccinationsNonInjecterPourAnimal(String idAnimal) {
        return vaccinationRepository.findVaccinsNonInjectesParAnimal( idAnimal)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VaccinationDTO getVaccinationById(int id) {
        Optional<Vaccination> vaccination = vaccinationRepository.findById(id);
        return vaccination.map(this::convertToDTO).orElse(null);
    }

    public VaccinationDTO addVaccination(VaccinationDTO vaccinationDTO) {
        Vaccination vaccination = convertToEntity(vaccinationDTO);
        Vaccination saved = vaccinationRepository.save(vaccination);
        return convertToDTO(saved);
    }

    public VaccinationDTO updateVaccination(int id, VaccinationDTO vaccinationDTO) {
        if (vaccinationRepository.existsById(id)) {
            Vaccination vaccination = convertToEntity(vaccinationDTO);
            vaccination.setIdVaccin(id);
            Vaccination updated = vaccinationRepository.save(vaccination);
            return convertToDTO(updated);
        }
        return null;
    }

    public void deleteVaccination(int id) {
        vaccinationRepository.deleteById(id);
    }

    // Conversion Entity → DTO
    private VaccinationDTO convertToDTO(Vaccination vaccination) {
        VaccinationDTO dto = new VaccinationDTO();
        dto.setIdVaccin(vaccination.getIdVaccin());
        dto.setVaccinNom(vaccination.getVaccinNom());
        dto.setCibleAge(vaccination.getCibleAge());
        dto.setCibleSexe(vaccination.getCibleSexe());
        dto.setRemarqueVaccination(vaccination.getRemarqueVaccination());
        return dto;
    }

    // Conversion DTO → Entity
    private Vaccination convertToEntity(VaccinationDTO dto) {
        Vaccination vaccination = new Vaccination();
        vaccination.setIdVaccin(dto.getIdVaccin());
        vaccination.setVaccinNom(dto.getVaccinNom());
        vaccination.setCibleAge(dto.getCibleAge());
        vaccination.setCibleSexe(dto.getCibleSexe());
        vaccination.setRemarqueVaccination(dto.getRemarqueVaccination());
        return vaccination;
    }

    public void ajouterVaccination(String idAnimal, AnimalVaccinationDTO dto) {
        // Récupérer l'animal
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new EntityNotFoundException("Animal non trouvé avec l'ID : " + idAnimal));

        // Vérifier que l'ID de la vaccination est valide
        Vaccination vaccination = vaccinationRepository.findById(dto.getIdVaccin())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination non trouvée avec l'ID : " + dto.getIdVaccin()));

        // Créer l'objet AnimalVaccination
        AnimalVaccination vaccin = new AnimalVaccination();
        vaccin.setDateVaccination(dto.getDateVaccination());
        vaccin.setVaccination(vaccination);
        vaccin.setAnimal(animal);

        // Enregistrer dans la base de données
        animalVaccinRepository.save(vaccin);
    }

}

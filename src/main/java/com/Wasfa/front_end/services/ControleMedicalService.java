package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Dto.ControlMedicalDTO;
import com.Wasfa.front_end.Entity.Animal;
import com.Wasfa.front_end.Entity.ControlMedical;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.ControlMedicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ControleMedicalService {

    private final AnimalRepository animalRepository;
    private final ControlMedicalRepository controlMedicalRepository;

    @Autowired
    public ControleMedicalService(AnimalRepository animalRepository, ControlMedicalRepository controlMedicalRepository) {
        this.animalRepository = animalRepository;
        this.controlMedicalRepository = controlMedicalRepository;
    }

    public ControlMedical addControlMedicalFromDTO(ControlMedicalDTO dto, String id) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + dto.getIdAnimal()));

        ControlMedical control = new ControlMedical();
        control.setDateControl(dto.getDateControl());
        control.setMaladie(dto.getMaladie());
        control.setTraitement(dto.getTraitement());
        control.setAnimal(animal);

        return controlMedicalRepository.save(control);
    }

//    // Optionnel : méthode de conversion si besoin
//    private ControlMedicalDTO convertToDTO(ControlMedical control) {
//        ControlMedicalDTO dto = new ControlMedicalDTO();
//        dto.setTraitement(control.getTraitement());
//        dto.setDateControl(control.getDateControl());
//        dto.setMaladie(control.getMaladie());
//        dto.setIdAnimal(control.getAnimal().getIdAnimal()); // à condition que getId() existe dans Animal
//        return dto;
//    }
}

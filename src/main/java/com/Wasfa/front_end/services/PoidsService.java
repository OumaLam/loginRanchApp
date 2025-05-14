package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Dto.HistoriquePoidsDTO;
import com.Wasfa.front_end.Entity.Animal;
import com.Wasfa.front_end.Entity.HistoriquePoids;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.HistoriquePoidsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoidsService {

    private final AnimalRepository animalRepository;
    private final HistoriquePoidsRepository poidsRepository;

    @Autowired
    public PoidsService(AnimalRepository animalRepository, HistoriquePoidsRepository poidsRepository) {
        this.animalRepository = animalRepository;
        this.poidsRepository = poidsRepository;
    }

    public void ajouterPoids(String idAnimal, HistoriquePoidsDTO dto) {
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new EntityNotFoundException("Animal non trouvé avec l'ID : " + idAnimal));

        HistoriquePoids poids = new HistoriquePoids();
        poids.setPoids(dto.getPoids());
        poids.setDateMesure(dto.getDateMesure());
        poids.setAnimal(animal);

        poidsRepository.save(poids);
    }

    public List<HistoriquePoids> getPoidsByAnimal(String idAnimal) {
        return poidsRepository.findByAnimalIdAnimal(idAnimal);
    }
}


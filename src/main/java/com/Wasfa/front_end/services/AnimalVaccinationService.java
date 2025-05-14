package com.Wasfa.front_end.services;

import com.Wasfa.front_end.Entity.AnimalVaccination;
import com.Wasfa.front_end.repository.AnimalVaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalVaccinationService {

    @Autowired
    private AnimalVaccinationRepository repository;

    public AnimalVaccination save(AnimalVaccination vaccination) {
        return repository.save(vaccination);
    }

    public List<AnimalVaccination> getAll() {
        return repository.findAll();
    }
}


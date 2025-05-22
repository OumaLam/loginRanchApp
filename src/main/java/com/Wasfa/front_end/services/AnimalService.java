package com.Wasfa.front_end.services;
import com.Wasfa.front_end.Dto.*;
import com.Wasfa.front_end.Entity.Animal;
import com.Wasfa.front_end.Entity.HistoriquePoids;
import com.Wasfa.front_end.Entity.StatutAnimal;
import com.Wasfa.front_end.Securite.CryptageAES;
import com.Wasfa.front_end.repository.AnimalRepository;
import com.Wasfa.front_end.repository.AnimalVaccinationRepository;
import com.Wasfa.front_end.repository.ControlMedicalRepository;
import com.Wasfa.front_end.repository.HistoriquePoidsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    @Value("${encryption.secret.key}")
    private String secretKey;

    @Autowired
    private final AnimalRepository animalRepository;
    private final AnimalVaccinationRepository animalVaccinationRepository;
    private final HistoriquePoidsRepository historiquePoidsRepository;
    private final ControlMedicalRepository controlMedicalRepository;


    public AnimalService(AnimalRepository animalRepository,
                         AnimalVaccinationRepository animalVaccinationRepository,
                         HistoriquePoidsRepository historiquePoidsRepository,
                         ControlMedicalRepository controlMedicalRepository) {
        this.animalRepository = animalRepository;
        this.animalVaccinationRepository = animalVaccinationRepository;
        this.historiquePoidsRepository = historiquePoidsRepository;
        this.controlMedicalRepository = controlMedicalRepository;
    }


    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAllWithStatut()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // *******************************statistique ****************************************
    public long countAnimals() {
        return animalRepository.count();
    }
    public Map<String, Long> getSexeDistribution() {
        List<Animal> animaux = animalRepository.findAll();
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("M", animaux.stream().filter(a -> "M".equalsIgnoreCase(a.getSexe())).count());
        distribution.put("F", animaux.stream().filter(a -> "F".equalsIgnoreCase(a.getSexe())).count());

        return distribution;
    }
    //********************************************************************************

    public AnimalDTO getAnimalById(String id) {
        Optional<Animal> animal = animalRepository.findById(id);
        return animal.map(this::convertToDTO).orElse(null);
    }



    public AnimalDTO updateAnimal(AnimalDTO dto) {
        Optional<Animal> optionalAnimal = animalRepository.findById(dto.getIdAnimal());

        if (!optionalAnimal.isPresent()) {
            throw new RuntimeException("Animal non trouvé avec ID : " + dto.getIdAnimal());
        }

        CryptageAES crypto = new CryptageAES(secretKey);
        Animal animal = optionalAnimal.get();

        // Mise à jour des champs
        animal.setSexe(dto.getSexe());
        animal.setRace(dto.getRace());
        animal.setDateNaissance(dto.getDateNaissance());

        // Mise à jour du statut
        if (dto.getStatut() != null) {
            StatutAnimal statut = animal.getStatut();
            if (statut == null) {
                statut = new StatutAnimal();
                statut.setAnimal(animal); // liaison avec l’animal
            }

            statut.setDateStatut(dto.getStatut().getDateStatut());
            statut.setStatutTitre(dto.getStatut().getStatutTitre());

            if (dto.getStatut().getPrix() != null) {
                //String prixChiffre = crypto.encrypt(dto.getStatut().getPrix().toString());
                statut.setPrix(dto.getStatut().getPrix());
            }

            statut.setCause(dto.getStatut().getCause());
            animal.setStatut(statut);
        }

        // Mise à jour du poids (ajout dans la liste)
        if (dto.getPoids() != null) {
            List<HistoriquePoids> poidsList = dto.getPoids().stream().map(pdto -> {
                HistoriquePoids poids = new HistoriquePoids();
                poids.setDateMesure(pdto.getDateMesure());
                poids.setPoids(crypto.encrypt(String.valueOf(pdto.getPoids())));
                poids.setAnimal(animal);
                return poids;
            }).collect(Collectors.toList());

            animal.getPoids().addAll(poidsList); // conserve l’historique
        }

        Animal updated = animalRepository.save(animal);
        return convertToDTO(updated);
    }

    public void deleteAnimal(String id) {
        animalRepository.deleteById(id);
    }

    private AnimalDTO convertToDTO(Animal animal) {
        AnimalDTO dto = new AnimalDTO();
        dto.setIdAnimal(animal.getIdAnimal());
        dto.setRace(animal.getRace());
        dto.setDateNaissance(animal.getDateNaissance());
        dto.setSexe(animal.getSexe());
        // Vérification si le statut existe
        if (animal.getStatut() != null) {
            dto.setStatutTitre(animal.getStatut().getStatutTitre());
            dto.setPrix(animal.getStatut().getPrix());
            dto.setCause(animal.getStatut().getCause());


            // Ajout du statut
        } else {
            dto.setStatutTitre("Non défini"); // Si pas de statut, on met "Non défini"
        }

        return dto;
    }
    private Animal convertToEntity(AnimalDTO dto) {
        Animal animal = new Animal();
        animal.setIdAnimal(dto.getIdAnimal());
        animal.setRace(dto.getRace());
        animal.setDateNaissance(dto.getDateNaissance());
        animal.setSexe(dto.getSexe());
        return animal;
    }

    public List<AnimalDTO> getLastAnimals() {
        List<Animal> lastAnimals = animalRepository.findTop5ByOrderByDateNaissanceDesc();
        return lastAnimals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public AnimalDTO addAnimal(AnimalDTO dto) {
        CryptageAES crypto = new CryptageAES(secretKey);

        Animal animal = new Animal();
       // String idAnimalChiff = crypto.encrypt(dto.getIdAnimal().toString());

        animal.setIdAnimal(dto.getIdAnimal());
        animal.setDateNaissance(dto.getDateNaissance());
        animal.setRace(dto.getRace());
        animal.setSexe(dto.getSexe());

        // StatutAnimal
        if (dto.getStatut() != null) {
            StatutAnimal statut = new StatutAnimal();
            statut.setDateStatut(dto.getStatut().getDateStatut());
            statut.setStatutTitre(dto.getStatut().getStatutTitre());

            if (dto.getStatut().getPrix() != null) {
                //String prixChiffre = crypto.encrypt(dto.getStatut().getPrix().toString());
                statut.setPrix(dto.getStatut().getPrix()); // on stocke le texte chiffré directement

            }

            statut.setCause(dto.getStatut().getCause());
            statut.setAnimal(animal);
            animal.setStatut(statut);
        }

        // HistoriquePoids
        if (dto.getPoids() != null) {
            List<HistoriquePoids> poidsList = dto.getPoids().stream().map(pdto -> {
                HistoriquePoids p = new HistoriquePoids();
                p.setDateMesure(pdto.getDateMesure());
                // Encrypt the poids and store it as a String
                //String poidsChiffre = crypto.encrypt(String.valueOf(pdto.getPoids()));
                p.setPoids(pdto.getPoids());  // Now it's a String field
                p.setAnimal(animal);
                return p;
            }).collect(Collectors.toList());
            animal.setPoids(poidsList);
        }



        Animal saved = animalRepository.save(animal);
        return convertToDTO(saved);
    }


    //***************************** Detailles Animal ********************************************

    public AnimalDetailsDTO getAnimalDetails(String animalId, String type, LocalDate date) {
        CryptageAES crypto = new CryptageAES(secretKey);
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal non trouvé"));

        AnimalDTO animalDTO = convertToDTO(animal);

        List<HistoriquePoidsDTO> poids = (type == null || type.equals("poids")) ?
                historiquePoidsRepository.findByAnimalIdAndDate(animalId, date)
                        .stream()
                        .map(p -> new HistoriquePoidsDTO(p.getIdPoid(), p.getDateMesure(),(p.getPoids())))
                        .toList()
                : List.of();

        List<VaccinationDTO> vaccinations = (type == null || type.equals("vaccination")) ?
                animalVaccinationRepository.findByAnimalIdAndDate(animalId, date)
                        .stream()
                        .map(v -> new VaccinationDTO(
                                v.getVaccination().getIdVaccin(),
                                v.getVaccination().getVaccinNom(),
                                v.getVaccination().getCibleAge(),
                                v.getVaccination().getCibleSexe(),
                                v.getVaccination().getRemarqueVaccination(),
                                v.getDateVaccination()
                                ))
                        .toList()
                : List.of();

        List<ControlMedicalDTO> controles = (type == null || type.equals("controle")) ?
                controlMedicalRepository.findByAnimalIdAndDate(animalId, date)
                        .stream()
                        .map(c -> new ControlMedicalDTO(c.getIdControle(), c.getDateControl(), c.getMaladie(), c.getTraitement()))
                        .toList()
                : List.of();


        return new AnimalDetailsDTO(animalDTO, controles, vaccinations,poids );
    }
}




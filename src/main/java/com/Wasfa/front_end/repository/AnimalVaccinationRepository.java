package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.AnimalVaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface AnimalVaccinationRepository extends JpaRepository<AnimalVaccination, Long> {
    @Query("SELECT a FROM AnimalVaccination a WHERE a.animal.id = :animalId "
            + "AND (:date IS NULL OR a.dateVaccination = :date)")
    List<AnimalVaccination> findByAnimalIdAndDate(@Param("animalId") String animalId,
                                                  @Param("date") LocalDate date);

    }

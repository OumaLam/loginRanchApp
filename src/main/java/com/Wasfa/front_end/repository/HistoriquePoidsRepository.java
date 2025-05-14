package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.HistoriquePoids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriquePoidsRepository extends JpaRepository<HistoriquePoids, Integer> {

    @Query("SELECT h FROM HistoriquePoids h WHERE h.animal.id = :animalId "
            + "AND (:date IS NULL OR h.dateMesure = :date)")
    List<HistoriquePoids> findByAnimalIdAndDate(@Param("animalId") String animalId,
                                                @Param("date") LocalDate date);

    List<HistoriquePoids> findByAnimalIdAnimal(String idAnimal);
}


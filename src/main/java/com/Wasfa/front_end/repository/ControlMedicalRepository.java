package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.ControlMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ControlMedicalRepository extends JpaRepository<ControlMedical, Integer> {

    @Query("SELECT c FROM ControlMedical c WHERE c.animal.id = :animalId "
            + "AND (:date IS NULL OR c.dateControl = :date)")
    List<ControlMedical> findByAnimalIdAndDate(@Param("animalId") String animalId,
                                               @Param("date") LocalDate date);


}


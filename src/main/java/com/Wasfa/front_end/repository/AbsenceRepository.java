package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    // Additional query methods can be added here
}

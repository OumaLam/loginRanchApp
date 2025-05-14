package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.StatutAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

interface  StatutRepository extends JpaRepository<StatutAnimal, Long> {
}

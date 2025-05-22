// EmployeRepositoryRanch.java
package com.Wasfa.front_end.repository;
import com.Wasfa.front_end.Entity.EmployeRanch;
import com.Wasfa.front_end.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeRepositoryRanch extends JpaRepository<EmployeRanch, String> {
    Optional<EmployeRanch> findByEmail(String email);

    // Pour obtenir directement le rôle d'un employé à partir de l'email
    @Query("SELECT e.roleEmploye FROM EmployeRanch e WHERE e.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);


    List<EmployeRanch> findByActifTrue();
    @Query("SELECT e FROM EmployeRanch e WHERE LOWER(e.cin) = LOWER(:cin)")
    Optional<EmployeRanch> findByCin(@Param("cin") String cin);

    Optional<EmployeRanch> findByCinHash(String cinHash);
}

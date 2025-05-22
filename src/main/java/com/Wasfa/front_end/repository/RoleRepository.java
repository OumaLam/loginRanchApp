package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByEmployeRanch_Cin(String cin);

    @Query("SELECT e.roleEmploye FROM EmployeRanch e WHERE e.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);


}

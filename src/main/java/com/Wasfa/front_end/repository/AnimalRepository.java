package com.Wasfa.front_end.repository;

import com.Wasfa.front_end.Dto.StatutDistributionDTO;
import com.Wasfa.front_end.Entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, String> {
    List<Animal> findTop5ByOrderByDateNaissanceDesc();
    // Additional query methods can be added here

    @Query("SELECT a FROM Animal a WHERE a.dateNaissance <= :dateMax " +
            "AND (:sexe = 'Tous' OR a.sexe = :sexe) " +
            "AND a.id NOT IN (SELECT av.animal.id FROM AnimalVaccination av WHERE av.vaccination.id = :vaccinId)")
    List<Animal> findAnimauxEligiblesNonVaccines(
            @Param("dateMax") LocalDate dateMax,
            @Param("sexe") String sexe,
            @Param("vaccinId") Long vaccinId
    );

    default List<Animal> findAllById(String idAnimal) {
        return null;
    }
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.statut")
    List<Animal> findAllWithStatut();
    @Query("SELECT a.statut.statutTitre, COUNT(a) FROM Animal a GROUP BY a.statut.statutTitre")
    Map<String, Long> countByStatutTitre();

//    @Query("SELECT a.sexe, COUNT(a) FROM Animal a GROUP BY a.sexe")
//    Map<String, Long> countBySexe();
long countBySexe(String sexe);
    @Query("SELECT new com.Wasfa.front_end.Dto.StatutDistributionDTO(a.statut.statutTitre, COUNT(a)) FROM Animal a GROUP BY a.statut.statutTitre")
    List<StatutDistributionDTO> countByStatut();
    @Query("SELECT MONTH(a.dateNaissance) as mois, COUNT(a) as count " +
            "FROM Animal a " +
            "WHERE a.statut.statutTitre = :statutTitre " +
            "GROUP BY MONTH(a.dateNaissance) " +
            "ORDER BY mois")
    List<Object[]> countByMonthAndStatut(@Param("statutTitre") String statutTitre);



}

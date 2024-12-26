package com.example.demo.repository;

import com.example.demo.Enum.Mois;
import com.example.demo.entities.Salaire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISalaireRepository extends CrudRepository<Salaire, Integer> {
    List<Salaire> findSalaireByMoisAndAnnee(Mois mois, int annee);

}

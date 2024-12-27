package com.example.demo.repository;

import com.example.demo.Enum.TypeContrat;
import com.example.demo.entities.Contrat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContratRepository extends CrudRepository<Contrat, Integer> {
    List<Contrat> findByTypeContrat(TypeContrat typeContrat);
}

package com.example.demo.repository;

import com.example.demo.entities.Contrat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContratRepository extends CrudRepository<Contrat, Integer> {
}

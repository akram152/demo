package com.example.demo.repository;

import com.example.demo.entities.Departement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartementRepository extends CrudRepository<Departement, Integer> {
}

package com.example.demo.repository;

import com.example.demo.entities.Salaire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISalaireRepository extends CrudRepository<Salaire, Integer> {
}

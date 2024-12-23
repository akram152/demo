package com.example.demo.repository;

import com.example.demo.entities.Collaborateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollaborateurRepository extends CrudRepository<Collaborateur, Integer> {

}

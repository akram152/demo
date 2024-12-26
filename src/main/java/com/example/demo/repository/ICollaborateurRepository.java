package com.example.demo.repository;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.entities.Collaborateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICollaborateurRepository extends CrudRepository<Collaborateur, Integer> {
    Collaborateur findByNomAndPrenom(String nom, String prenom);
    List<Collaborateur> findByNiveau(niveauEtude niveau);
    Collaborateur findByRole(Role role);

}

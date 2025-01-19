package com.example.demo.repository;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.Enum.nomPoste;
import com.example.demo.entities.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICollaborateurRepository extends JpaRepository<Collaborateur, Integer> {
    Collaborateur findByNomAndPrenom(String nom, String prenom);

    List<Collaborateur> findByNiveau(niveauEtude niveau);

    //Collaborateur findByRole(Role role);

    Optional<Collaborateur> findByEmail(String email);

    List<Collaborateur> findCollaborateurByNomPoste(nomPoste nomPoste);

    Collaborateur findCollaborateurByTelephone(int telephone);

    Boolean existsByEmail(String email);

    Optional<Collaborateur> findByUsername(String username);

}

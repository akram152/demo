package com.example.demo.services;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.Enum.nomPoste;
import com.example.demo.entities.Collaborateur;

import java.util.List;
import java.util.Optional;

public interface ICollaborateurService {
    Collaborateur addCollaborateur(Collaborateur collaborateur);

    Collaborateur getCollaborateurById(int id);

    List<Collaborateur> getAllCollaborateurs();

    Collaborateur updateCollaborateur(Collaborateur collaborateur);

    void deleteCollaborateur(int id);

    Collaborateur findByNomAndPrenom(String nom, String prenom);

    List<Collaborateur> findByNiveau(niveauEtude niveau);

   // Collaborateur findByRole(Role role);

    Collaborateur assignCollaborateurDepartement(int idCollaborateur, int idDepartement);

    Optional<Collaborateur> findByEmail(String email);

    List<Collaborateur> findCollaborateurByNomPoste(nomPoste nomPoste);

    Collaborateur findCollaborateurByTelephone(int telephone);



}

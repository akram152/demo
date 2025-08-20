package com.example.demo.services;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutValidation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.responses.ExpertResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUtilisateurService {
    Utilisateur addUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateurById(int id);

    List<Utilisateur> getAllUtilisateurs();

    Utilisateur updateUtilisateur(Utilisateur utilisateur);

    void deleteUtilisateur(int id);

    Utilisateur findByNomAndPrenom(String nom, String prenom);

    Optional<Utilisateur> findByEmail(String email);

    Utilisateur findUtilisateurByTelephone(String telephone);

    List<Utilisateur> findExpertsByRoleAndStatut();

     void validerExpert(int id);

    void refuserExpert(int id);

    List<Utilisateur> findByRoles_NameClient();

    void deleteClient(int id);

    List<Utilisateur> findByRoles_NameExpert();

    long countUtilisateurs();

    long countUtilisateurByRoles_Name();

    long countUtilisateurByStatutValidation();

    long countUtilisateurByStatutValidationn();

    long countUtilisateurByStatutRefuse();

    Utilisateur getCurrentUtilisateur();

    boolean isExpert(Utilisateur utilisateur);

    boolean hasRole(Utilisateur utilisateur, Role role);

    List<Utilisateur> findByDomaineExpertiseAndStatutValidation(String domaineExpertise);

    List<String> findDistinctDomaineExpertiseAndStatutValidation( );

    Double calculerMoyenneTarifExpertsValides();

    Double calculerMoyenneExperienceExpertsValides();








}

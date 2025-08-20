package com.example.demo.repository;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutValidation;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IUtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByNomAndPrenom(String nom, String prenom);

    Optional<Utilisateur> findByEmail(String email);

    Utilisateur findUtilisateurByTelephone(String telephone);

    Boolean existsByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM Utilisateur u JOIN u.roles r WHERE r.name = :role AND u.statutValidation = :statut")
    List<Utilisateur> findExpertsByRoleAndStatut(@Param("role") Role role, @Param("statut") StatutValidation statutValidation);

    List<Utilisateur> findByRoles_Name(Role rolesName);

    List<Utilisateur> findByStatutValidation(StatutValidation statutValidation);

    @Query("SELECT COUNT(u) FROM Utilisateur u JOIN u.roles r WHERE r.name <> 'ADMIN'")
    long count();

    long countUtilisateurByRoles_Name(Role rolesName);

    long countUtilisateurByStatutValidation(StatutValidation statutValidation);

    List<Utilisateur> findByDomaineExpertiseIgnoreCaseAndStatutValidation(String domaineExpertise, StatutValidation statutValidation);

    @Query("SELECT DISTINCT u.domaineExpertise FROM Utilisateur u WHERE u.statutValidation = :statut AND u.domaineExpertise IS NOT NULL")
    List<String> findDistinctDomainesExpertiseValides(@Param("statut") StatutValidation statut);








}

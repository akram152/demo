package com.example.demo.entities;

import com.example.demo.Enum.StatutValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    private String password;
    private String adresse;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String anneesExperience;
    private String certification;
    @Pattern(regexp = "^(https://)?(www\\.)?linkedin\\.com/in/.*$", message = "Lien LinkedIn invalide")
    private String linkedinUrl;
    private String domaineExpertise;
    private String cvPath;
    private String bio;
    private String imageProfil;
    private String age; 
    private String tarif;
    private double noteMoyenne;
    @Enumerated(EnumType.STRING)
    private StatutValidation statutValidation;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();
    private String username;
    private Boolean isActive;


    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Reservation> reservationsEffectuees;
    @JsonIgnore
    @OneToMany(mappedBy = "expert")
    private List<Reservation> reservationsRecues;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Avis> avisLaisses;
    @JsonIgnore
    @OneToMany(mappedBy = "expert")
    private List<Avis> avisRecus;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Paiement> paiements;
}

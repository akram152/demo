package com.example.demo.entities;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.natureEtude;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.Enum.nomPoste;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collaborateur")
public class Collaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    private String password;
    private String adresse;
    @Email(message = "L'email doit être valide.")
    private String email;
    @Digits(integer = 8, fraction = 0, message = "Le numéro de téléphone doit contenir exactement 8 chiffres.")
    private int telephone;
    @Past(message = "La date de naissance doit être dans le passé.")
    private Date dateNaissance;
    private String numDeCompte;
    private String numSecurite;
    @Enumerated(EnumType.STRING)
    private nomPoste nomPoste;
    @Enumerated(EnumType.STRING)
    private niveauEtude niveau;
    @Enumerated(EnumType.STRING)
    private natureEtude nature;
    @OneToMany(mappedBy = "collaborateur")
    @JsonIgnore
    private List<Salaire> salaire;
    @OneToMany(mappedBy = "collaborateur")
    @JsonIgnore
    List<Contrat> contrat;
    @ManyToOne
    Departement departement;
    @OneToMany(mappedBy = "collaborateur")
    @JsonIgnore
    List<Mission> mission;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "collaborateur_roles",
            joinColumns = @JoinColumn(name = "collaborateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();
    private String username;
}

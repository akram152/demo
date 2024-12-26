package com.example.demo.entities;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.natureEtude;
import com.example.demo.Enum.niveauEtude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collaborateur")

public class Collaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String prenom;
    private String password;
    private String adresse;
    private String email;
    private int telephone;
    private Date dateNaissance;
    private int numDeCompte;
    private int numSecurite;
    @Enumerated(EnumType.STRING)
    private niveauEtude niveau;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private natureEtude nature;
    @OneToMany(mappedBy = "collaborateur")
    private List<Salaire> salaire;
    @OneToMany(mappedBy = "collaborateur")
    @JsonIgnore
    List<Contrat> contrat;
    @ManyToOne
    Departement departement;
    @OneToMany(mappedBy = "collaborateur")
    List<Mission> mission;


}

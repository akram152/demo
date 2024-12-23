package com.example.demo.entities;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.natureEtude;
import com.example.demo.Enum.niveauEtude;
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

public class Collaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    private Role role;
    @Enumerated(EnumType.STRING)
    private natureEtude nature;
    @Enumerated(EnumType.STRING)
    private niveauEtude niveau;
    @OneToMany(mappedBy = "collaborateur")
    private List<Salaire> salaire;
    @OneToMany(mappedBy = "collaborateur")
    List<Contrat> contrat;
    @ManyToOne
    Departement departement;
    @OneToMany(mappedBy = "collaborateur")
    List<Mission> mission;


}

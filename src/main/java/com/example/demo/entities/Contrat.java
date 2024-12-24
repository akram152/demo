package com.example.demo.entities;

import com.example.demo.Enum.TypeContrat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrat")
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;
    private float salaireBase;
    private Date dateDebutContrat;
    private Date dateFinContrat;
    @ManyToOne
    Collaborateur collaborateur;
}


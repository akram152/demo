package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "avis")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int note;
    private double noteMoyenne;// de 1 à 5
    @Column(length = 1000)
    private String commentaire;
    private LocalDateTime dateCreation;
    @ManyToOne
    private Utilisateur client;

    @ManyToOne
    private Utilisateur expert;

    @OneToOne
    private Consultation consultation;
}

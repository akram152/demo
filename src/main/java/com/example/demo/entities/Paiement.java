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
@Table(name = "paiement")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime datePaiement;
    private Double montant;
    private String methode; // "Carte bancaire", "D17", etc.
    private boolean estValide;

    @ManyToOne
    private Utilisateur client;

    @OneToOne
    private Consultation consultation;

}

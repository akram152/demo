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
@Table(name = "consultation")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime dateDebutEffective;
    private LocalDateTime dateFinEffective;
    private String resume;

    private String lienVisio; // lien de la visioconférence s’il y en a un

    private boolean estTerminee;
    private boolean enCours;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private Avis avis;

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private Paiement paiement;




}

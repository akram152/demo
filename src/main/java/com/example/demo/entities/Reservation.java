package com.example.demo.entities;

import com.example.demo.Enum.StatutReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // Date et heure de la réservation
    private LocalDate dateRendezvous;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    @Enumerated(EnumType.STRING)
    private StatutReservation statutReservation;
    private LocalDateTime dateCreation;
    private String messageClient;
    private boolean rappelEnvoye;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Utilisateur expert;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Consultation consultation;
}

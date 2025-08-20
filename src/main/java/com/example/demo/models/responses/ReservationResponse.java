package com.example.demo.models.responses;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponse {
    private long id;
    private LocalDate dateRendezvous;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String messageClient;
    private String statutReservation;

    private int clientId;
    private String clientNom;
    private String clientPrenom;
    private String clientEmail;

    private int expertId;
    private String expertNom;
    private String expertPrenom;
    private String expertEmail;
    private String imageProfil;





}

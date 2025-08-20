package com.example.demo.models.requests;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    private int expertId;
    private LocalDate dateRendezvous;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String messageClient;
}

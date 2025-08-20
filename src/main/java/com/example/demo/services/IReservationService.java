package com.example.demo.services;

import com.example.demo.models.requests.ReservationRequest;
import com.example.demo.models.responses.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IReservationService {
    List<LocalDateTime> getCreneauxDisponibles(int expertId, LocalDate date);

    void reserver(ReservationRequest request);

    List<ReservationResponse> getReservationsExpert(LocalDate date);

    void traiterReservation(long reservationId, boolean accepter);

    void annulerReservation(long reservationId);

    List<ReservationResponse> getAgendaExpert();

    List<ReservationResponse> getReservationsClient(String statut, Boolean passes,LocalDate date);



}

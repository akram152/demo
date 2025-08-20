package com.example.demo.utils.mappers;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.responses.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationMappers {
    public ReservationResponse toReservationResponse(Reservation reservation) {
        ReservationResponse dto = new ReservationResponse();
        dto.setId(reservation.getId());
        dto.setDateRendezvous(reservation.getDateRendezvous());
        dto.setHeureDebut(reservation.getHeureDebut());
        dto.setHeureFin(reservation.getHeureFin());
        dto.setMessageClient(reservation.getMessageClient());
        dto.setStatutReservation(reservation.getStatutReservation().name());

        Utilisateur client = reservation.getClient();
        dto.setClientId(client.getId());
        dto.setClientNom(client.getNom());
        dto.setClientPrenom(client.getPrenom());
        dto.setClientEmail(client.getEmail());
        dto.setImageProfil(client.getImageProfil());

        return dto;
    }

    public ReservationResponse toReservationResponses(Reservation reservation) {
        ReservationResponse dto = new ReservationResponse();

        dto.setId(reservation.getId());
        dto.setDateRendezvous(reservation.getDateRendezvous());
        dto.setHeureDebut(reservation.getHeureDebut());
        dto.setHeureFin(reservation.getHeureFin());
        dto.setStatutReservation(reservation.getStatutReservation().name());
        dto.setMessageClient(reservation.getMessageClient());

        // Récupérer expert depuis la réservation
        Utilisateur expert = reservation.getExpert();
        if (expert != null) {
            dto.setExpertId(expert.getId());
            dto.setExpertNom(expert.getNom());
            dto.setExpertPrenom(expert.getPrenom());
            dto.setExpertEmail(expert.getEmail());
            dto.setImageProfil(expert.getImageProfil());
        }

        return dto;
    }



}

package com.example.demo.utils.mappers;

import com.example.demo.Enum.StatutReservation;
import com.example.demo.entities.Reservation;
import com.example.demo.repository.IReservationRepository;
import com.example.demo.services.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final INotificationService notificationService;
    private final IReservationRepository IReservationRepository;
    private static final ZoneId ZONE_TUNIS = ZoneId.of("Africa/Tunis");

    @Scheduled(fixedRate = 300000) // toutes les 5 minutes
    public void envoyerNotificationsRappelConsultations() {
        // Heure et date actuelles en Tunisie
        LocalDate dateActuelle = ZonedDateTime.now(ZONE_TUNIS).toLocalDate();
        LocalTime heureActuelle = ZonedDateTime.now(ZONE_TUNIS).toLocalTime();

        // Fenêtre : de l'heure actuelle jusqu'à 1 heure plus tard
        LocalTime heureFin = heureActuelle.plusHours(1);

        // Chercher les réservations dans la fenêtre
        List<Reservation> reservations = IReservationRepository.findByDateAndTimeBetween(dateActuelle, heureActuelle, heureFin);

        for (Reservation reservation : reservations) {

                if (!reservation.isRappelEnvoye() && reservation.getStatutReservation() == StatutReservation.ACCEPTEE) {
                    notificationService.envoyerNotification(
                            reservation.getClient(),
                            "Rappel : votre consultation est prévue à " + reservation.getHeureDebut() + " aujourd'hui "
                    );
                    notificationService.envoyerNotification(
                            reservation.getExpert(),
                            "Rappel : vous avez une consultation à préparer à " + reservation.getHeureDebut() + " aujourd'hui "
                    );

                    // Marquer comme rappel envoyé
                    reservation.setRappelEnvoye(true);
                    IReservationRepository.save(reservation);
                }

        }
    }
}
package com.example.demo.services.impl;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutReservation;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.requests.ReservationRequest;
import com.example.demo.models.responses.ReservationResponse;
import com.example.demo.repository.IReservationRepository;
import com.example.demo.repository.IUtilisateurRepository;
import com.example.demo.services.INotificationService;
import com.example.demo.services.IReservationService;
import com.example.demo.services.IUtilisateurService;
import com.example.demo.utils.mappers.ReservationMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {
    private final IReservationRepository reservationRepository;
    private final IUtilisateurService utilisateurService;
    private final IUtilisateurRepository utilisateurRepository;
    private final ReservationMappers reservationMappers;
    private final INotificationService notificationService;

    public List<LocalDateTime> getCreneauxDisponibles(int expertId,LocalDate date) {
        Utilisateur expert = utilisateurRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert introuvable"));

        boolean isExpert = expert.getRoles().stream()
                .anyMatch(role -> role.getName() == Role.EXPERT);
        if (!isExpert) {
            throw new RuntimeException("L'utilisateur n'est pas un expert");
        }

        List<Reservation> reservations = reservationRepository
                .findByExpertIdAndDateRendezvous(expert.getId(), date)
                .stream()
                .filter(res -> res.getStatutReservation() != StatutReservation.REFUSEE)
                .collect(Collectors.toList());

        List<LocalTime> creneauxPossibles = new ArrayList<>();
        LocalTime time = LocalTime.of(8, 0);
        while (!time.isAfter(LocalTime.of(19, 30))) {
            creneauxPossibles.add(time);
            time = time.plusMinutes(30);
        }

        List<LocalTime> creneauxDispo = creneauxPossibles.stream()
                .filter(heure -> reservations.stream().noneMatch(res -> {
                    LocalTime debut = res.getHeureDebut();
                    LocalTime finAvecRepos = res.getHeureFin().plusMinutes(30);
                    LocalTime finCreneau = heure.plusMinutes(30);
                    return heure.isBefore(res.getHeureFin().plusMinutes(30)) &&
                            heure.plusMinutes(30).isAfter(res.getHeureDebut());
                }))
                .collect(Collectors.toList());

        return creneauxDispo.stream()
                .map(t -> LocalDateTime.of(date, t))
                .collect(Collectors.toList());
    }

    @Override
    public void reserver(ReservationRequest request) {
        Utilisateur client = utilisateurService.getCurrentUtilisateur();

        // 🔒 Vérifier que le client est bien un "CLIENT"
        if (!utilisateurService.hasRole(client, Role.CLIENT)) {
            throw new RuntimeException("Seuls les clients peuvent effectuer une réservation.");
        }

        // ✅ Vérifier que l'utilisateur ciblé est bien un expert
        Utilisateur expert = utilisateurRepository.findById(request.getExpertId())
                .orElseThrow(() -> new RuntimeException("Expert introuvable"));

        if (!utilisateurService.hasRole(expert, Role.EXPERT)) {
            throw new RuntimeException("Le rendez-vous doit être pris avec un expert.");
        }

        // ❌ Vérifier le chevauchement (plus intelligent si tu as heureDebut et heureFin)
        boolean conflit = reservationRepository.existsByExpertAndDateRendezvousAndOverlap(
                expert.getId(),
                request.getDateRendezvous(),
                request.getHeureDebut(),
                request.getHeureFin()
        );

        if (conflit) {
            throw new RuntimeException("Ce créneau est déjà réservé.");
        }

        // ✅ Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setDateRendezvous(request.getDateRendezvous());
        reservation.setHeureDebut(request.getHeureDebut());
        reservation.setHeureFin(request.getHeureFin());
        reservation.setExpert(expert);
        reservation.setClient(client);
        reservation.setDateCreation(LocalDateTime.now());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);
        reservation.setMessageClient(request.getMessageClient());

        reservationRepository.save(reservation);
        notificationService.envoyerNotification(
                expert,
                "Nouvelle réservation de " + client.getNom() + " " + client.getPrenom() +
                        " pour le " + reservation.getDateRendezvous() + " à " + reservation.getHeureDebut() + "au" + reservation.getHeureFin()
        );

    }

    @Override
    public List<ReservationResponse> getReservationsExpert(LocalDate date) {
        Utilisateur expert = utilisateurService.getCurrentUtilisateur();

        if (!utilisateurService.isExpert(expert)) {
            throw new RuntimeException("Accès refusé. Vous n'êtes pas un expert.");
        }

        List<Reservation> reservations = (date != null) ?
                reservationRepository.findByExpertIdAndDateRendezvous(expert.getId(), date) :
                reservationRepository.findByExpertId(expert.getId());

        return reservations.stream()
                .map(reservationMappers::toReservationResponse)  // ou mapper.toReservationResponse
                .collect(Collectors.toList());
    }

    @Override
    public void traiterReservation(long reservationId, boolean accepter) {
        Utilisateur expert = utilisateurService.getCurrentUtilisateur();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        // Vérifier que la réservation appartient à cet expert
        if (reservation.getExpert().getId() != expert.getId()) {
            throw new RuntimeException("Accès refusé : Cette réservation ne vous appartient pas.");
        }

        if (accepter) {
            reservation.setStatutReservation(StatutReservation.ACCEPTEE);
            reservationRepository.save(reservation);
            notificationService.envoyerNotification(
                    reservation.getClient(),
                    "Votre réservation du " + reservation.getDateRendezvous() + " à " + reservation.getHeureDebut() + "au" + reservation.getHeureFin() +
                            " a été acceptée par " + expert.getNom() + " " + expert.getPrenom()
            );

        } else {
            reservation.setStatutReservation(StatutReservation.REFUSEE);
            reservationRepository.save(reservation);
            notificationService.envoyerNotification(
                    reservation.getClient(),
                    "Votre réservation du " + reservation.getDateRendezvous() + " à " + reservation.getHeureDebut() +
                            " a été refusée par " + expert.getNom() + " " + expert.getPrenom());
            // Supprimer la réservation (optionnel)

        }

    }

    @Override
    public void annulerReservation(long reservationId) {
        Utilisateur client = utilisateurService.getCurrentUtilisateur();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        // Vérifie si le client est bien le propriétaire de la réservation
        if (reservation.getClient().getId() != client.getId()) {
            throw new RuntimeException("Accès refusé : vous n'êtes pas le client de cette réservation.");
        }

        // Vérifie que le rendez-vous n'est pas passé
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debutRDV = LocalDateTime.of(reservation.getDateRendezvous(), reservation.getHeureDebut());

        if (debutRDV.isBefore(maintenant)) {
            throw new RuntimeException("Impossible d'annuler une réservation passée.");
        }

        reservationRepository.delete(reservation);
        notificationService.envoyerNotification(
                reservation.getExpert(),
                "La réservation du " + reservation.getDateRendezvous() + " à " + reservation.getHeureDebut() +
                        " a été annulée par " + client.getNom() + " " + client.getPrenom()
        );
    }

    @Override
    public List<ReservationResponse> getReservationsClient(String statut, Boolean passes, LocalDate date) {
        Utilisateur client = utilisateurService.getCurrentUtilisateur();

        List<Reservation> reservations = reservationRepository.findByClient_Id(client.getId());

        LocalDateTime now = LocalDateTime.now();

        return reservations.stream()
                .filter(res -> {
                    // 🔹 Filtrage par statut explicite
                    if (statut != null && !res.getStatutReservation().name().equalsIgnoreCase(statut)) {
                        return false;
                    }

                    // 🔹 Filtrage par passes (passées ou futures)
                    if (passes != null) {
                        LocalDateTime dateHeure = LocalDateTime.of(res.getDateRendezvous(), res.getHeureDebut());

                        if (passes) {
                            return dateHeure.isBefore(now) && res.getStatutReservation() == StatutReservation.ACCEPTEE;
                        } else {
                            return dateHeure.isAfter(now);
                        }
                    }

                    // 🔹 Filtrage par date exacte
                    if (date != null && !res.getDateRendezvous().isEqual(date)) {
                        return false;
                    }

                    return true;
                })
                .map(reservationMappers::toReservationResponses)
                .collect(Collectors.toList());
    }


    @Override
    public List<ReservationResponse> getAgendaExpert() {
        Utilisateur expert = utilisateurService.getCurrentUtilisateur();

        boolean isExpert = expert.getRoles().stream()
                .anyMatch(role -> role.getName() == Role.EXPERT);
        if (!isExpert) {
            throw new RuntimeException("Accès refusé. Vous n'êtes pas un expert.");
        }

        List<Reservation> reservationsConfirmées = reservationRepository
                .findByExpert_IdAndStatutReservation(expert.getId(), StatutReservation.ACCEPTEE);

        // Mapper en DTO
        return reservationsConfirmées.stream()
                .map(reservationMappers::toReservationResponse)
                .sorted(Comparator.comparing(r -> LocalDateTime.of(r.getDateRendezvous(), r.getHeureDebut())))
                .collect(Collectors.toList());
    }
}



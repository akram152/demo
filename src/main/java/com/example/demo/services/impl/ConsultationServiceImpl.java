package com.example.demo.services.impl;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutReservation;
import com.example.demo.entities.Consultation;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.responses.ConsultationResponse;
import com.example.demo.repository.IConsultationRepository;
import com.example.demo.repository.IReservationRepository;
import com.example.demo.services.IConsultationService;
import com.example.demo.services.INotificationService;
import com.example.demo.services.IUtilisateurService;
import com.example.demo.utils.mappers.ConsultationMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements IConsultationService {
    private final IConsultationRepository consultationRepository;
    private final IReservationRepository reservationRepository;
    private final IUtilisateurService utilisateurService;
    private final MailService emailService;
    private final ConsultationMapper consultationMapper;
    private final FileStorageService fileStorageService;
    private static final long TOLERANCE_MINUTES = 10;
    private final INotificationService notificationService;

    @Override
    public ConsultationResponse startConsultation(long reservationId) {
        // 1) Vérifier la réservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        if (reservation.getStatutReservation() != StatutReservation.ACCEPTEE) {
            throw new RuntimeException("La réservation n'est pas acceptée.");
        }
        if (reservation.getConsultation() != null) {
            throw new RuntimeException("Une consultation existe déjà pour cette réservation.");
        }

        // 2) Contrôle de la fenêtre temporelle
        LocalDateTime dateDebutPrevue = LocalDateTime.of(
                reservation.getDateRendezvous(),
                reservation.getHeureDebut()
        );

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(dateDebutPrevue.minusMinutes(TOLERANCE_MINUTES))) {
            throw new RuntimeException("Trop tôt: vous pouvez lancer la session "
                    + TOLERANCE_MINUTES + "min avant l'heure prévue.");
        }
        if (now.isAfter(dateDebutPrevue.plusMinutes(TOLERANCE_MINUTES))) {
            throw new RuntimeException("Trop tard: la session devait commencer il y a plus de "
                    + TOLERANCE_MINUTES + "min.");
        }

        // 3) Création de la consultation
        Consultation consultation = new Consultation();
        consultation.setDateDebutEffective(dateDebutPrevue);
        consultation.setEstTerminee(false);
        consultation.setLienVisio(generateJitsiLink());
        consultation.setReservation(reservation);

        Consultation saved = consultationRepository.save(consultation);

        // 4) Lier à la réservation (si mapping bidirectionnel)
        reservation.setConsultation(saved);
        reservationRepository.save(reservation);

        // 5) Notification e‑mail
        sendConsultationEmails(reservation.getClient(), reservation.getExpert(), saved);
        consultationRepository.save(consultation);
        notificationService.envoyerNotification(
                reservation.getClient(),
                "Nouvelle consultation prévue à " + reservation.getHeureDebut() + " le " + reservation.getDateRendezvous()
        );

        notificationService.envoyerNotification(
                reservation.getExpert(),
                "Nouvelle consultation à préparer le " + reservation.getDateRendezvous()
        );
        return consultationMapper.toDto(consultation);
    }

    @Override
    public String generateJitsiLink() {
        String roomName = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return "https://meet.jit.si/" + roomName;
    }

    @Override
    public void sendConsultationEmails(Utilisateur client, Utilisateur expert, Consultation consultation) {
        String sujet = "Lien de consultation –" +
                consultation.getDateDebutEffective().toLocalDate();

        String corps = """
                Bonjour %s,

                Votre session de consultation est programmée le %s à %s.

                Lien de visioconférence: %s

                Merci de vous connecter quelques minutes à l'avance.
                """.formatted(
                "%s %s".formatted(client.getPrenom(), client.getNom()),
                consultation.getDateDebutEffective().toLocalDate(),
                consultation.getDateDebutEffective().toLocalTime(),
                consultation.getLienVisio()
        );// Au client
        emailService.envoyerEmail(client.getEmail(), sujet, corps);
    }

    @Override
    public void endConsultation(long consultationId) {
        Utilisateur expert = utilisateurService.getCurrentUtilisateur();

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation introuvable"));

        // Vérifier que l’expert connecté est bien celui de la réservation
        if (consultation.getReservation().getExpert().getId()!= expert.getId()) {
            throw new RuntimeException("Accès refusé : cette consultation ne vous appartient pas.");
        }

        // Vérifier si déjà terminée
        if (consultation.isEstTerminee()) {
            throw new RuntimeException("Cette consultation est déjà terminée.");
        }

        // Enregistrer la fin de la consultation
        consultation.setDateFinEffective(LocalDateTime.now());
        consultation.setEstTerminee(true);

        consultationRepository.save(consultation);
    }

    @Override
    public List<ConsultationResponse> getConsultationsHistorique() {
        Utilisateur user = utilisateurService.getCurrentUtilisateur();

        List<Consultation> consultations;

        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(Role.CLIENT))) {
            // Toutes les consultations du client, terminées ou non
            consultations = consultationRepository.findConsultationByReservation_Client_Id(user.getId());
        } else if (user.getRoles().stream().anyMatch(r -> r.getName().equals(Role.EXPERT))) {
            // Toutes les consultations de l'expert, terminées ou non
            consultations = consultationRepository.findConsultationByReservation_Expert_Id(user.getId());
        } else {
            throw new RuntimeException("Accès refusé");
        }

        return consultations.stream()
                .map(consultationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(long consultationId, MultipartFile file) {
        Consultation consultation = getConsultationOrThrow(consultationId);
        checkParticipant(consultation);

        return fileStorageService.storePartageFile(file, consultationId);
    }

    public List<String> listFiles(long consultationId) throws IOException {
        Consultation consultation = getConsultationOrThrow(consultationId);
        checkParticipant(consultation);

        Path dir = Paths.get("uploads").resolve("consult_" + consultationId);
        if (Files.notExists(dir)) return List.of();

        return Files.list(dir)
                .filter(Files::isRegularFile)
                .map(dir::relativize)
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public Resource downloadFile(long consultationId, String fileName) throws IOException {

        Consultation consultation = getConsultationOrThrow(consultationId);
        checkParticipant(consultation);
        verifierAccesFichier(consultationId);

        Path filePath = Paths.get("uploads")
                .resolve("consult_" + consultationId)
                .resolve(fileName)
                .normalize();

        if (Files.notExists(filePath)) {
            throw new RuntimeException("Fichier introuvable");
        }

        return new UrlResource(filePath.toUri());
    }

    @Override
    public void verifierAccesFichier(long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation introuvable"));

        LocalDateTime now = LocalDateTime.now();

        if (consultation.getDateDebutEffective() == null ) {
            throw new IllegalStateException("La consultation n'a pas encore démarré.");
        }

        if (now.isBefore(consultation.getDateDebutEffective())) {
            throw new IllegalStateException("La consultation n'a pas encore commencé.");
        }
    }

    @Override
    public void ajouterResume(long consultationId, String resume) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation introuvable"));

        Utilisateur current = utilisateurService.getCurrentUtilisateur();

        // 3. Vérifier droits : expert concerné OU admin
        boolean isAdmin  = current.getRoles().stream().anyMatch(r -> r.getName() == Role.ADMIN);
        boolean isOwner  = consultation.getReservation()
                .getExpert()
                .getId() == current.getId();

        if (!(isOwner || isAdmin)) {
            throw new RuntimeException("Accès refusé: vous n’êtes pas l’expert de cette consultation.");
        }

        // 4. Vérifier que la consultation est terminée
        if (!consultation.isEstTerminee()) {
            throw new RuntimeException("Vous ne pouvez ajouter un résumé que pour une consultation terminée.");
        }

        consultation.setResume(resume);
        consultationRepository.save(consultation);
    }

    @Override
    public ConsultationResponse findConsultationById(long id) {
        Consultation consultation = getConsultationOrThrow(id);
        return consultationMapper.toDto(consultation);
    }

    @Override
    public long getNombreConsultationsPourUtilisateur() {
        Utilisateur user = utilisateurService.getCurrentUtilisateur();

        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(Role.ADMIN))) {
            return consultationRepository.count();}
        else if (user.getRoles().stream().anyMatch(r -> r.getName().equals(Role.CLIENT))) {
            return consultationRepository.countByReservation_Client_Id(user.getId());
        } else if (user.getRoles().stream().anyMatch(r -> r.getName().equals(Role.EXPERT))) {
            return consultationRepository.countByReservation_Expert_Id(user.getId());
        }
        else {
            throw new RuntimeException("Accès refusé");
        }

    }

    private Consultation getConsultationOrThrow(long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation introuvable"));
    }

    private void checkParticipant(Consultation c) {
        Utilisateur me = utilisateurService.getCurrentUtilisateur();
        boolean ok = me.getId() == c.getReservation().getExpert().getId()
                || me.getId() == c.getReservation().getClient().getId();
        if (!ok) throw new RuntimeException("Accès refusé : vous n’êtes pas participant.");
    }


}
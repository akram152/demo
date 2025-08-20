package com.example.demo.services.impl;

import com.example.demo.Enum.Role;
import com.example.demo.entities.Avis;
import com.example.demo.entities.Consultation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.requests.AvisRequest;
import com.example.demo.models.responses.AvisResponse;
import com.example.demo.repository.IAvisRepository;
import com.example.demo.repository.IConsultationRepository;
import com.example.demo.repository.IUtilisateurRepository;
import com.example.demo.services.IAvisService;
import com.example.demo.services.INotificationService;
import com.example.demo.services.IUtilisateurService;
import com.example.demo.utils.mappers.AvisMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AvisServiceImpl implements IAvisService {

    private final IAvisRepository avisRepository;
    private final IConsultationRepository consultationRepository;
    private final IUtilisateurService utilisateurService;
    private final IUtilisateurRepository utilisateurRepository;
    private final AvisMappers avisMapper;
    private final INotificationService notificationService;


    @Override
    public AvisResponse ajouterAvis(AvisRequest request) {
        Utilisateur client = utilisateurService.getCurrentUtilisateur();

        if (!utilisateurService.hasRole(client, Role.CLIENT)) {
            throw new RuntimeException("Seuls les clients peuvent effectuer les avis.");
        }

        Consultation consultation = consultationRepository.findById(request.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consultation introuvable"));

        if (!consultation.isEstTerminee()) {
            throw new RuntimeException("La consultation n’est pas encore terminée.");
        }

        if (consultation.getReservation().getClient().getId()!=(client.getId())) {
            throw new RuntimeException("Vous ne pouvez pas évaluer cette consultation.");
        }

        if (avisRepository.findByConsultation_Id(consultation.getId()).isPresent()) {
            throw new RuntimeException("Un avis existe déjà pour cette consultation.");
        }

        Utilisateur expert = consultation.getReservation().getExpert();

        Avis avis = avisMapper.toEntity(request, consultation, client, expert);
        Avis saved = avisRepository.save(avis);

        // Recalcul de la note moyenne de l’expert
        List<Avis> avisExpert = avisRepository.findAvisByExpert_Id(expert.getId());

        double moyenne = avisExpert.stream()
                .mapToDouble(Avis::getNote)
                .average()
                .orElse(0.0);

        expert.setNoteMoyenne(moyenne);
        utilisateurRepository.save(expert); // Mettre à jour l'utilisateur expert

        // Notification
        notificationService.envoyerNotification(
                expert,
                "Un nouveau avis a été ajouté pour la consultation du " +
                        consultation.getDateDebutEffective().toLocalDate()
        );

        return avisMapper.toDto(saved);
    }


    @Override
    public AvisResponse getAvisByConsultation(long consultationId) {
        Utilisateur current = utilisateurService.getCurrentUtilisateur();
        if (!utilisateurService.hasRole(current, Role.EXPERT)) {
            throw new RuntimeException("Seuls les experts peuvent voir les avis.");
        }
        Avis avis = avisRepository.findByConsultation_Id(consultationId)
                .orElseThrow(() -> new RuntimeException("Avis introuvable"));

        if (current.getId() != avis.getExpert().getId() && current.getId() != avis.getClient().getId()) {
            throw new RuntimeException("Accès refusé");
        }
        return avisMapper.toDto(avis);
    }

    @Override
    public List<AvisResponse> getAvisForExpert() {
        Utilisateur expert = utilisateurService.getCurrentUtilisateur();
        if (!utilisateurService.hasRole(expert, Role.EXPERT)) {
            throw new RuntimeException("Seuls les experts peuvent voir les avis.");
        }
        List<Avis> avisList = avisRepository.findAvisByExpert_Id(expert.getId());
        return avisList.stream()
                .map(avisMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean avisDejaDonnePourConsultation(long consultationId) {
        Utilisateur client = utilisateurService.getCurrentUtilisateur();
        if (!utilisateurService.hasRole(client, Role.CLIENT)) {
            throw new RuntimeException("Seuls les clients peuvent voir les avis.");
        }
        return avisRepository.existsByConsultation_IdAndClient_Id(consultationId, client.getId());

    }
}


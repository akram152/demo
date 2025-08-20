package com.example.demo.services.impl;

import com.example.demo.entities.Notification;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.services.INotificationService;
import com.example.demo.services.IUtilisateurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final IUtilisateurService utilisateurService;

    @Override
    public void envoyerNotification(Utilisateur destinataire, String contenu) {
        Notification notification = new Notification();
        notification.setContenu(contenu);
        notification.setDestinataire(destinataire);
        notification.setDateCreation(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getMesNotifications() {
        Utilisateur utilisateur = utilisateurService.getCurrentUtilisateur();
        return notificationRepository.findByDestinataire_IdOrderByDateCreationDesc(utilisateur.getId());
    }

    @Override
    public void marquerCommeLue(long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        n.setLue(true);
        notificationRepository.save(n);
    }
}


package com.example.demo.services;

import com.example.demo.entities.Notification;
import com.example.demo.entities.Utilisateur;

import java.util.List;

public interface INotificationService {
    void envoyerNotification(Utilisateur destinataire, String contenu);

    List<Notification> getMesNotifications();

    void marquerCommeLue(long id);
}

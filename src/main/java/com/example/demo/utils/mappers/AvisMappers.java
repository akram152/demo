package com.example.demo.utils.mappers;

import com.example.demo.entities.Avis;
import com.example.demo.entities.Consultation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.requests.AvisRequest;
import com.example.demo.models.responses.AvisResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AvisMappers {
    public Avis toEntity(AvisRequest request, Consultation consultation, Utilisateur client, Utilisateur expert) {
        Avis avis = new Avis();
        avis.setNote(request.getNote());
        avis.setCommentaire(request.getCommentaire());
        avis.setDateCreation(LocalDateTime.now());
        avis.setConsultation(consultation);
        avis.setClient(client);
        avis.setExpert(expert);
        return avis;
    }

    public AvisResponse toDto(Avis avis) {
        AvisResponse response = new AvisResponse();
        response.setId(avis.getId());
        response.setNote(avis.getNote());
        response.setNoteMoyenne(avis.getNoteMoyenne());
        response.setCommentaire(avis.getCommentaire());
        response.setDateCreation(avis.getDateCreation());
        response.setClientNomPrenom(avis.getClient().getPrenom() + " " + avis.getClient().getNom());
        response.setExpertNomPrenom(avis.getExpert().getPrenom() + " " + avis.getExpert().getNom());
        if (avis.getClient() != null) {
            response.setImageProfil(avis.getClient().getImageProfil());
        } else {
            response.setImageProfil(null);
        }
        return response;
    }
}

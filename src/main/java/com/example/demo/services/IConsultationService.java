package com.example.demo.services;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.responses.ConsultationResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IConsultationService {
    ConsultationResponse startConsultation(long reservationId);

    String generateJitsiLink();
    void sendConsultationEmails(Utilisateur client,
                                Utilisateur expert,
                                Consultation consultation);

    void endConsultation(long consultationId);

    List<ConsultationResponse> getConsultationsHistorique();

    String uploadFile(long consultationId, MultipartFile file);

    List<String> listFiles(long consultationId) throws IOException;

    Resource downloadFile(long consultationId, String fileName) throws IOException;

    void verifierAccesFichier(long consultationId);

    void ajouterResume(long consultationId, String resume);

    ConsultationResponse findConsultationById(long id);

    long getNombreConsultationsPourUtilisateur();


}

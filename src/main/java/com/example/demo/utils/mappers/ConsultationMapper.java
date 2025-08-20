package com.example.demo.utils.mappers;

import com.example.demo.entities.Consultation;
import com.example.demo.models.responses.ConsultationResponse;
import com.example.demo.repository.IAvisRepository;
import com.example.demo.services.IAvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsultationMapper {
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private final IAvisRepository avisRepository;
    public ConsultationResponse toDto(Consultation c) {
        ConsultationResponse dto = new ConsultationResponse();
        dto.setId(c.getId());
        dto.setDateDebutEffective(c.getDateDebutEffective());
        dto.setDateFinEffective(c.getDateFinEffective());
        dto.setLienVisio(c.getLienVisio());
        dto.setEstTerminee(c.isEstTerminee());
        dto.setReservationId(c.getReservation().getId());
        dto.setLienVisio(c.isEstTerminee() ? null : c.getLienVisio());
        dto.setResume(c.getResume());
        dto.setEnCours(c.isEnCours());
        boolean avisExiste = avisRepository.existsByConsultationId(c.getId());
        dto.setAvisDejaLaisse(avisExiste);

        if (c.getReservation() != null) {
            if (c.getReservation().getClient() != null) {
                var client = c.getReservation().getClient();
                dto.setClientNomPrenom(client.getPrenom() + " " + client.getNom());

                if (client.getImageProfil() != null) {
                    dto.setClientImageUrl("http://localhost:9090/uploads/" + client.getImageProfil());
                }
            }
            if (c.getReservation().getExpert() != null) {
                var expert = c.getReservation().getExpert();
                dto.setExpertNomPrenom(expert.getPrenom() + " " + expert.getNom());

                if (expert.getImageProfil() != null) {
                    dto.setExpertImageUrl("http://localhost:9090/uploads/" + expert.getImageProfil());
                }
            }
        }

        Path dossier = fileStorageLocation.resolve("consult_" + c.getId());
        if (Files.exists(dossier) && Files.isDirectory(dossier)) {
            try {
                List<String> fichiers = Files.list(dossier)
                        .map(path -> "consult_" + c.getId() + "/" + path.getFileName().toString())
                        .collect(Collectors.toList());
                dto.setFichiersPartages(fichiers);
            } catch (Exception e) {
                dto.setFichiersPartages(List.of()); // en cas d'erreur
            }
        } else {
            dto.setFichiersPartages(List.of()); // Aucun fichier
        }

        return dto;
    }
}

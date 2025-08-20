package com.example.demo.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationResponse {
    private long id;
    private LocalDateTime dateDebutEffective;
    private LocalDateTime dateFinEffective;
    private String lienVisio;
    private boolean estTerminee;
    private long reservationId;
    private String clientNomPrenom;
    private String expertNomPrenom;
    private List<String> fichiersPartages;
    private String resume;
    private boolean enCours;
    private boolean avisDejaLaisse;
    private String clientImageUrl;
    private String expertImageUrl;
}
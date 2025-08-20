package com.example.demo.models.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvisResponse {
    private long id;
    private int note;
    private double noteMoyenne;
    private String commentaire;
    private String clientNomPrenom;
    private String expertNomPrenom;
    private LocalDateTime dateCreation;
    private String imageProfil;
}

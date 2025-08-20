package com.example.demo.models.responses;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class ExpertResponse {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String anneesExperience;
    private String certification;
    private String domaineExpertise;
    private String cvFile;
    private String bio;
    private String imageProfil;
}

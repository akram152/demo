package com.example.demo.models.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class ExpertRequest {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String anneesExperience;
    private String certification;
    private String domaineExpertise;
    private MultipartFile cvFile;
    private String bio;
    private MultipartFile imageProfil;
}

package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.entities.Roles;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private Set<String> role;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private LocalDate dateNaissance;
    private String anneesExperience;
    private String certification;
    private String domaineExpertise;
    private MultipartFile cvFile;
    private String bio;
    private MultipartFile imageProfil;
    private String age;
    private String tarif;
    private String linkedinUrl;
    private Boolean isActive;
}
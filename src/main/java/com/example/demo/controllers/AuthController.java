package com.example.demo.controllers;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutValidation;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Utilisateur;
import com.example.demo.models.JwtResponse;
import com.example.demo.models.LoginRequest;
import com.example.demo.models.SignupRequest;
import com.example.demo.repository.IUtilisateurRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.security.config.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.services.INotificationService;
import com.example.demo.services.impl.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final IUtilisateurRepository iUtilisateurRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final FileStorageService fileStorageService;
    private final INotificationService notificationService;

    public AuthController(AuthenticationManager authenticationManager, IUtilisateurRepository iUtilisateurRepository, RolesRepository rolesRepository, PasswordEncoder encoder, JwtUtils jwtUtils,INotificationService notificationService ) {
        this.authenticationManager = authenticationManager;
        this.iUtilisateurRepository = iUtilisateurRepository;
        this.rolesRepository = rolesRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.notificationService = notificationService;
        this.fileStorageService = new FileStorageService();
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Utilisateur utilisateur = iUtilisateurRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        if (!utilisateur.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Utilisateur inactif!");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping(value = "/signup" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignupRequest signUpRequest ) {

        if (iUtilisateurRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nom d'utilisateur existe déjà!"));
        }
        if (iUtilisateurRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email existe déjà!"));
        }
        // Create new user's account
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(signUpRequest.getUsername());
        utilisateur.setEmail(signUpRequest.getEmail());
        utilisateur.setPassword(encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Roles> roles = new HashSet<>();
        if (strRoles == null) {
            Roles userRole = rolesRepository.findByName(Role.ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toUpperCase()) {
                    case "CLIENT":
                        LocalDate dateNaissance = signUpRequest.getDateNaissance();
                        LocalDate aujourdHui = LocalDate.now();

                        if (dateNaissance == null) {
                            throw new RuntimeException("La date de naissance est obligatoire.");
                        }

                        int age = Period.between(dateNaissance, aujourdHui).getYears();

                        if (age < 20) {
                            throw new RuntimeException("Vous devez avoir au moins 20 ans pour vous inscrire.");
                        }
                        Roles adminRole = rolesRepository.findByName(Role.CLIENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        utilisateur.setIsActive(Boolean.TRUE);
                        utilisateur.setAdresse(signUpRequest.getAdresse());
                        utilisateur.setTelephone(signUpRequest.getTelephone());
                        utilisateur.setNom(signUpRequest.getNom());
                        utilisateur.setPrenom(signUpRequest.getPrenom());
                        utilisateur.setDateNaissance(dateNaissance);
                        if (signUpRequest.getImageProfil() != null && !signUpRequest.getImageProfil().isEmpty()) {
                            String imagePath = fileStorageService.storeFile(signUpRequest.getImageProfil(), "image");
                            utilisateur.setImageProfil(imagePath);
                        }

                        break;
                    case "ADMIN":
                        Roles userRole = rolesRepository.findByName(Role.ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        utilisateur.setIsActive(Boolean.TRUE);

                        break;
                    case "EXPERT":
                        Roles experRole = rolesRepository.findByName(Role.EXPERT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        utilisateur.setIsActive(Boolean.FALSE);
                        utilisateur.setStatutValidation(StatutValidation.EN_ATTENTE);
                        roles.add(experRole);
                        utilisateur.setTelephone(signUpRequest.getTelephone());
                        utilisateur.setNom(signUpRequest.getNom());
                        utilisateur.setPrenom(signUpRequest.getPrenom());
                        utilisateur.setDateNaissance(signUpRequest.getDateNaissance());
                        utilisateur.setCertification(signUpRequest.getCertification());
                        utilisateur.setDomaineExpertise(signUpRequest.getDomaineExpertise());
                        utilisateur.setAnneesExperience(signUpRequest.getAnneesExperience());
                        utilisateur.setBio(signUpRequest.getBio());
                        utilisateur.setTarif(signUpRequest.getTarif());
                        utilisateur.setLinkedinUrl(signUpRequest.getLinkedinUrl());
                        if (signUpRequest.getImageProfil() != null && !signUpRequest.getImageProfil().isEmpty()) {
                            String imagePath = fileStorageService.storeFile(signUpRequest.getImageProfil(), "image");
                            utilisateur.setImageProfil(imagePath);
                        }
                        List<Utilisateur> admins = iUtilisateurRepository.findByRoles_Name(Role.ADMIN);
                        for (Utilisateur admin : admins) {
                            notificationService.envoyerNotification(
                                    admin,
                                    "expert en attente:" + utilisateur.getNom() + " " + utilisateur.getPrenom()
                            );
                        }

// Stocker le fichier CV
                        if (signUpRequest.getCvFile() != null && !signUpRequest.getCvFile().isEmpty()) {
                            String cvPath = fileStorageService.storeFile(signUpRequest.getCvFile(), "pdf");
                            utilisateur.setCvPath(cvPath);

                        }
                        break;
                }
            });
        }
        utilisateur.setRoles(roles);
        iUtilisateurRepository.save(utilisateur);
        return ResponseEntity.ok(Map.of("message", "Utilisateur registered successfully!"));
    }
}
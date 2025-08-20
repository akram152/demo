package com.example.demo.controllers;

import com.example.demo.entities.Utilisateur;
import com.example.demo.repository.IUtilisateurRepository;
import com.example.demo.services.IUtilisateurService;
import com.example.demo.services.impl.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("utilisateur")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {
    @Autowired
    IUtilisateurService utilisateurService;
    @Autowired
    IUtilisateurRepository utilisateurRepository;
    @Autowired
    FileStorageService fileStorageService;



    @PostMapping("addUtilisateur")
    public Utilisateur addUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.addUtilisateur(utilisateur);
    }

    @GetMapping("getById/{id}")
    public Utilisateur getUtilisateurById(@PathVariable("id") int id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @GetMapping("getAllCollab")
    public List<Utilisateur> getAllCollab() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PatchMapping("PutCollab")
    public Utilisateur putUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.updateUtilisateur(utilisateur);
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteUtilisateur(@PathVariable("id") int id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @GetMapping("getByNomEtPrenom/{nom}/{prenom}")
    public Utilisateur getByNomEtPrenom(@PathVariable("nom") String nom, @PathVariable("prenom") String prenom) {
        return utilisateurService.findByNomAndPrenom(nom, prenom);
    }

    @GetMapping("getByEmail/{email}")
    public Optional<Utilisateur> getByEmail(@PathVariable("email") String email) {
        return utilisateurService.findByEmail(email);
    }

    @GetMapping("getByTelephone/{telephone}")
    public Utilisateur getByTelephone(@PathVariable("telephone") String telephone) {
        return utilisateurService.findUtilisateurByTelephone(telephone);
    }

    @GetMapping("/getExpertEnAttente")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utilisateur> getExpertEnAttente() {
        return utilisateurService.findExpertsByRoleAndStatut();
    }

    @PutMapping("/validerExpert/{id}")
    public void validerExpert(@PathVariable("id") int id) {
        utilisateurService.validerExpert(id);
    }

    @PutMapping("/refusExpert/{id}")
    public void refusExpert(@PathVariable("id") int id) {
        utilisateurService.refuserExpert(id);
    }

    @GetMapping("/listClient")
    public List<Utilisateur> listClient() {
        return utilisateurService.findByRoles_NameClient();
    }

    @DeleteMapping("/deleteClient/{id}")
    public void deleteClient(@PathVariable("id") int id) {
        utilisateurService.deleteClient(id);
    }

    @GetMapping("/listExpertValider")
    public List<Utilisateur> listExpertValider() {
        return utilisateurService.findByRoles_NameExpert();
    }

    @GetMapping("/nombreUtilisateurs")
    public long listNombreUtilisateurs() {
        return utilisateurService.countUtilisateurs();
    }

    @GetMapping("/nombreClient")
    public long listNombreClient() {
        return utilisateurService.countUtilisateurByRoles_Name();
    }

    @GetMapping("/nombreExpertValider")
    public long NombreExpertValider() {
        return utilisateurService.countUtilisateurByStatutValidation();
    }

    @GetMapping("/nombreExpertValiderEnAttente")
    public long NombreExpertValiderEnAttente() {
        return utilisateurService.countUtilisateurByStatutValidationn();
    }

    @GetMapping("/nombreExpertValiderRefuser")
    public long NombreExpertValiderRefuser() {
        return utilisateurService.countUtilisateurByStatutRefuse();
    }

    @GetMapping("/getMesInfo")
    public Utilisateur getMesInfo() {
        return utilisateurService.getCurrentUtilisateur();

    }

    @PutMapping("/PutMesInfo")
    public Utilisateur putMesInfo(@RequestBody Utilisateur updatedData) {
        Utilisateur current = utilisateurService.getCurrentUtilisateur();

        if (updatedData.getNom() != null) current.setNom(updatedData.getNom());
        if (updatedData.getPrenom() != null) current.setPrenom(updatedData.getPrenom());
        if (updatedData.getAdresse() != null) current.setAdresse(updatedData.getAdresse());
        if (updatedData.getTelephone() != null) current.setTelephone(updatedData.getTelephone());
        if (updatedData.getDateNaissance() != null) current.setDateNaissance(updatedData.getDateNaissance());
        if (updatedData.getBio() != null) current.setBio(updatedData.getBio());
        if (updatedData.getDomaineExpertise() != null) current.setDomaineExpertise(updatedData.getDomaineExpertise());
        if (updatedData.getCertification() != null) current.setCertification(updatedData.getCertification());
        if (updatedData.getAnneesExperience() != null) current.setAnneesExperience(updatedData.getAnneesExperience());
        if (updatedData.getAge() != null) current.setAge(updatedData.getAge());
        return utilisateurRepository.save(current);
    }

    @PutMapping("/me/updatefichiers")
        public ResponseEntity<String> updateProfileFiles(
                @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
                @RequestParam(value = "imageProfil", required = false) MultipartFile imageProfil
        ) {
            Utilisateur current = utilisateurService.getCurrentUtilisateur();

            if (cvFile != null && !cvFile.isEmpty()) {
                String cvPath = fileStorageService.storeFile(cvFile, "pdf");
                current.setCvPath(cvPath);
            }

            if (imageProfil != null && !imageProfil.isEmpty()) {
                String imagePath = fileStorageService.storeFile(imageProfil, "image");
                current.setImageProfil(imagePath);
            }

            utilisateurRepository.save(current);
            return ResponseEntity.ok("Fichiers mis à jour avec succès.");
        }

        @GetMapping("/getDomaineByExpert/{expertise}")
           public List<Utilisateur> getDomaineByExpert(@PathVariable String expertise) {
           return utilisateurService.findByDomaineExpertiseAndStatutValidation(expertise);


        }

        @GetMapping("/domaines")
           public List<String> listDomaineExpertise() {
           return utilisateurService.findDistinctDomaineExpertiseAndStatutValidation();
        }

    @GetMapping("/moyenneTarif")
    public ResponseEntity<Double> getMoyenneTarif() {
        Double moyenne = utilisateurService.calculerMoyenneTarifExpertsValides();
        return ResponseEntity.ok(moyenne != null ? moyenne : 0.0);
    }

    @GetMapping("/moyenneexperience")
    public ResponseEntity<Double> getMoyenneExperience() {
        Double moyenne = utilisateurService.calculerMoyenneExperienceExpertsValides();
        return ResponseEntity.ok(moyenne != null ? moyenne : 0.0);
    }





}
package com.example.demo.controllers;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.Enum.nomPoste;
import com.example.demo.entities.Collaborateur;
import com.example.demo.services.ICollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("collaborateur")
@CrossOrigin(origins = "http://localhost:4200")
public class CollaborateurController {
    @Autowired
    ICollaborateurService collaborateurService;


    @PostMapping("addCollaborateur")
    @PreAuthorize("hasAuthority('DAF')")
    public Collaborateur addCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.addCollaborateur(collaborateur);
    }

    @GetMapping("getById/{id}")
    public Collaborateur getCollaborateurById(@PathVariable("id") int id) {
        return collaborateurService.getCollaborateurById(id);
    }

    @GetMapping("getAllCollab")
    public List<Collaborateur> getAllCollab() {
        return collaborateurService.getAllCollaborateurs();
    }

    @PatchMapping("PutCollab")
    public Collaborateur putCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.updateCollaborateur(collaborateur);
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteCollaborateur(@PathVariable("id") int id) {
        collaborateurService.deleteCollaborateur(id);
    }

    @GetMapping("getByNomEtPrenom/{nom}/{prenom}")
    public Collaborateur getByNomEtPrenom(@PathVariable("nom") String nom, @PathVariable("prenom") String prenom) {
        return collaborateurService.findByNomAndPrenom(nom, prenom);
    }

    @GetMapping("getByNiveauEtude/{niveau}")
    public List<Collaborateur> getByNiveauEtude(@PathVariable("niveau") niveauEtude niveau) {
        return collaborateurService.findByNiveau(niveau);
    }

/*
    @GetMapping("getByRole/{role}")
    public Collaborateur getByRole(@PathVariable("role") Role role) {
        return collaborateurService.findByRole(role);
    }
*/

    @PutMapping("assignCollabDepartement/{idCollaborateur}/{idDepartement}")
    public Collaborateur assignContratCollab(@PathVariable("idCollaborateur") int idCollaborateur, @PathVariable("idDepartement") int idDepartement) {
        return collaborateurService.assignCollaborateurDepartement(idCollaborateur, idDepartement);
    }

    @GetMapping("getByEmail/{email}")
    public Optional<Collaborateur> getByEmail(@PathVariable("email") String email) {
        return collaborateurService.findByEmail(email);
    }

    @GetMapping("getByNomPoste/{nomPoste}")
    public List<Collaborateur> getByNomPoste(@PathVariable("nomPoste") nomPoste nomPoste) {
        return collaborateurService.findCollaborateurByNomPoste(nomPoste);
    }

    @GetMapping("getByTelephone/{telephone}")
    public Collaborateur getByTelephone(@PathVariable("telephone") int telephone) {
        return collaborateurService.findCollaborateurByTelephone(telephone);
    }

}
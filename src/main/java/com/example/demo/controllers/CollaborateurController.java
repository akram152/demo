package com.example.demo.controllers;

import com.example.demo.entities.Collaborateur;
import com.example.demo.services.ICollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("collaborateur")
public class CollaborateurController {
    @Autowired
    ICollaborateurService collaborateurService;

    @PostMapping("addCollaborateur")
    public Collaborateur addCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.addCollaborateur(collaborateur);
    }

    @GetMapping("getById/{id}")
    public Collaborateur getCollaborateurById(@PathVariable int id) {
        return collaborateurService.getCollaborateurById(id);
    }

    @GetMapping("getAllCollab")
    public List<Collaborateur> getAllCollab() {
        return  collaborateurService.getAllCollaborateurs();
    }

    @PutMapping("PutCollab")
    public Collaborateur putCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.updateCollaborateur(collaborateur);
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteCollaborateur(@PathVariable("id") int id) {
        collaborateurService.deleteCollaborateur(id);
    }









}

package com.example.demo.controllers;

import com.example.demo.entities.Collaborateur;
import com.example.demo.services.ICollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("collaborateur")
public class CollaborateurController {
    @Autowired
    ICollaborateurService collaborateurService;
    @PostMapping("addCollaborateur")
    public Collaborateur addCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.addCollaborateur(collaborateur);
    }


}

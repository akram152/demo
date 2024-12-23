package com.example.demo.services;

import com.example.demo.entities.Collaborateur;
import com.example.demo.repository.ICollaborateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollaborateurServiceImpl implements ICollaborateurService {
    @Autowired
    ICollaborateurRepository collaborateurRepository;
    @Override
    public Collaborateur addCollaborateur(Collaborateur collaborateur) {
        return collaborateurRepository.save(collaborateur);
    }
}

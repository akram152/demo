package com.example.demo.services;

import com.example.demo.entities.Collaborateur;

import java.util.List;

public interface ICollaborateurService {
    public Collaborateur addCollaborateur(Collaborateur collaborateur);
    public Collaborateur getCollaborateurById(int id);
    public List<Collaborateur> getAllCollaborateurs();
    public Collaborateur updateCollaborateur(Collaborateur collaborateur);
    public void deleteCollaborateur(int id);
}

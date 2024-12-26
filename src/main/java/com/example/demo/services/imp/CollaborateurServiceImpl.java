package com.example.demo.services.imp;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.entities.Collaborateur;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.services.interfac.ICollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaborateurServiceImpl implements ICollaborateurService {
    @Autowired
    ICollaborateurRepository collaborateurRepository;

    @Override
    public Collaborateur addCollaborateur(Collaborateur collaborateur) {
        return collaborateurRepository.save(collaborateur);
    }

    @Override
    public Collaborateur getCollaborateurById(int id) {
        return collaborateurRepository.findById(id).get();
    }

    @Override
    public List<Collaborateur> getAllCollaborateurs() {
        return (List<Collaborateur>) collaborateurRepository.findAll();
    }

    @Override
    public Collaborateur updateCollaborateur(Collaborateur collaborateur) {
        return collaborateurRepository.save(collaborateur);
    }

    @Override
    public void deleteCollaborateur(int id) {
        collaborateurRepository.deleteById(id);

    }

    @Override
    public Collaborateur findByNomAndPrenom(String nom, String prenom) {
        return collaborateurRepository.findByNomAndPrenom(nom, prenom);
    }

    @Override
    public List<Collaborateur> findByNiveau(niveauEtude niveau) {
        return collaborateurRepository.findByNiveau(niveau);
    }

    @Override
    public Collaborateur findByRole(Role role) {
        return collaborateurRepository.findByRole(role);
    }
}

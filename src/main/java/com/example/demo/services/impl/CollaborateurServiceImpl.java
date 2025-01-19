package com.example.demo.services.impl;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.niveauEtude;
import com.example.demo.Enum.nomPoste;
import com.example.demo.entities.Collaborateur;
import com.example.demo.entities.Departement;
import com.example.demo.entities.Roles;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.repository.IDepartementRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.services.ICollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CollaborateurServiceImpl implements ICollaborateurService {
    @Autowired
    ICollaborateurRepository collaborateurRepository;

    @Autowired
    IDepartementRepository iDepartementRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolesRepository rolesRepository;

    @Override
    public Collaborateur addCollaborateur(Collaborateur collaborateur) {
        String encodedPassword = passwordEncoder.encode(collaborateur.getPassword());
        collaborateur.setPassword(encodedPassword);
        Set<Roles> roles = new HashSet<>();

        Roles devRole = rolesRepository.findByName(Role.DEV).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(devRole);
        collaborateur.setRoles(roles);

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
        Collaborateur collaborateur1 = collaborateurRepository.findById(collaborateur.getId()).get();
        collaborateur.setRoles(collaborateur1.getRoles());
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

/*    @Override
    public Collaborateur findByRole(Role role) {
        return collaborateurRepository.findByRole(role);
    }*/

    @Override
    public Collaborateur assignCollaborateurDepartement(int idCollaborateur, int idDepartement) {
        Collaborateur collaborateur = collaborateurRepository.findById(idCollaborateur).get();
        Departement departement = iDepartementRepository.findById(idDepartement).get();
        collaborateur.setDepartement(departement);
        return collaborateurRepository.save(collaborateur);
    }

    @Override
    public Optional<Collaborateur> findByEmail(String email) {
        return collaborateurRepository.findByEmail(email);
    }

    @Override
    public List<Collaborateur> findCollaborateurByNomPoste(nomPoste nomPoste) {
        return collaborateurRepository.findCollaborateurByNomPoste(nomPoste);
    }

    @Override
    public Collaborateur findCollaborateurByTelephone(int telephone) {
        return collaborateurRepository.findCollaborateurByTelephone(telephone);
    }


}

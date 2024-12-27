package com.example.demo.services.impl;

import com.example.demo.Enum.TypeContrat;
import com.example.demo.entities.Collaborateur;
import com.example.demo.entities.Contrat;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.repository.IContratRepository;
import com.example.demo.services.IContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratServiceImpl implements IContratService {
    @Autowired
    IContratRepository iContratRepository;
    @Autowired
    ICollaborateurRepository iCollaborateurRepository;

    @Override
    public Contrat addEtModifyContrat(Contrat contrat) {
        return iContratRepository.save(contrat);
    }

    @Override
    public List<Contrat> getAllContrat() {
        return (List<Contrat>) iContratRepository.findAll();
    }

    @Override
    public Contrat getContratById(int id) {
        return iContratRepository.findById(id).get();
    }

    @Override
    public void deleteContratById(int id) {
        iContratRepository.deleteById(id);
    }

    @Override
    public Contrat assignContratCollaborator(int idContrat, int idCollaborator) {
        Contrat contrat = iContratRepository.findById(idContrat).get();
        Collaborateur collab = iCollaborateurRepository.findById(idCollaborator).get();
        contrat.setCollaborateur(collab);

        return iContratRepository.save(contrat);
    }

    @Override
    public List<Contrat> findByTypeContrat(TypeContrat typeContrat) {
        return iContratRepository.findByTypeContrat(typeContrat);
    }
}

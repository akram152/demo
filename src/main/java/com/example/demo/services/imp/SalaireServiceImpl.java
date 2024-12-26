package com.example.demo.services.imp;

import com.example.demo.Enum.Mois;
import com.example.demo.entities.Collaborateur;
import com.example.demo.entities.Salaire;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.repository.ISalaireRepository;
import com.example.demo.services.interfac.ISalaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaireServiceImpl implements ISalaireService {
    @Autowired
    private ISalaireRepository salaireRepository;
    @Autowired
    private ICollaborateurRepository collaborateurRepository;

    @Override
    public List<Salaire> getAllSalaires() {
        return (List<Salaire>) salaireRepository.findAll();

    }

    @Override
    public Salaire getSalaireById(int id) {
        return salaireRepository.findById(id).orElse(null);
    }

    @Override
    public Salaire saveOrUpdateSalaire(Salaire salaire) {
        return salaireRepository.save(salaire);
    }

    @Override
    public void deleteSalaire(int id) {
        salaireRepository.deleteById(id);
    }

    @Override
    public void deleteAllSalaire() {
        salaireRepository.deleteAll();
    }

    @Override
    public Salaire assignSalaireCollaborateur(int idSalaire, int idCollaborateur) {
        Salaire salaire = salaireRepository.findById(idSalaire).get();
        Collaborateur collaborateur = collaborateurRepository.findById(idCollaborateur).get();
        salaire.setCollaborateur(collaborateur);

        return salaireRepository.save(salaire);
    }


}
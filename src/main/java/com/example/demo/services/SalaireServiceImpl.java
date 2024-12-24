package com.example.demo.services;

import com.example.demo.entities.Salaire;
import com.example.demo.repository.ISalaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaireServiceImpl implements ISalaireService {
    @Autowired
    private ISalaireRepository salaireRepository;

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


}
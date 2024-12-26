package com.example.demo.services.impl;

import com.example.demo.entities.Departement;
import com.example.demo.repository.IDepartementRepository;
import com.example.demo.services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementServiceImpl implements IDepartementService {

    @Autowired
    private IDepartementRepository departementRepository;

    @Override
    public Departement getDepartementById(int id) {
        return departementRepository.findById(id).orElse(null);
    }

    @Override
    public List<Departement> getAllDepartements() {
        return (List<Departement>) departementRepository.findAll();

    }

    @Override
    public List<Departement> findDepartementByNomDepartement(String name) {
        return departementRepository.findDepartementByNomDepartement(name);
    }


    @Override
    public Departement saveOrUpdateDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    @Override
    public void DeleteDepartement(int id) {
        departementRepository.deleteById(id);
    }


}

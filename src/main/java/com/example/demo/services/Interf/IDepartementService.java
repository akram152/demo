package com.example.demo.services.Interf;

import com.example.demo.entities.Departement;
import com.example.demo.entities.Salaire;

import java.util.List;

public interface IDepartementService {

    Departement getDepartementById(int id);

    List<Departement> getAllDepartements();

    List<Departement> findDepartementByNomDepartement(String name);

    Departement saveOrUpdateDepartement (Departement departement);

    void DeleteDepartement (int id);
}
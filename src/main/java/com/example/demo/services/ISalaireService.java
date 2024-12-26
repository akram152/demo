package com.example.demo.services;

import com.example.demo.entities.Salaire;

import java.util.List;

public interface ISalaireService {
    // Récupérer tous les salaires
    List<Salaire> getAllSalaires();

    // Récupérer un salaire par son ID
    Salaire getSalaireById(int id);

    // Ajouter ou mettre à jour un salaire
    Salaire saveOrUpdateSalaire(Salaire salaire);

    // Supprimer un salaire par son ID
    void deleteSalaire(int id);
    void deleteAllSalaire();
    Salaire assignSalaireCollaborateur(int idSalaire, int idCollaborateur);





    // Trouver les salaires d'un collaborateur pour une année donnée


    // Trouver les salaires supérieurs à un montant brut donné
}

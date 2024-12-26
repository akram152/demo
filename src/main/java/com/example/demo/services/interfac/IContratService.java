package com.example.demo.services.interfac;

import com.example.demo.entities.Contrat;

import java.util.List;

public interface IContratService {
    Contrat addEtModifyContrat(Contrat contrat);
    List<Contrat> getAllContrat();
    Contrat getContratById(int id);
    void deleteContratById(int id);
    Contrat assignContratCollaborator(int idContrat, int idCollaborator);



}

package com.example.demo.controllers;

import com.example.demo.entities.Contrat;
import com.example.demo.services.IContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("contrat")
public class ContratController {
    @Autowired
    IContratService icontratService;

    @PostMapping
    public Contrat addContrat(@RequestBody Contrat contrat) {
        return icontratService.addEtModifyContrat(contrat);
    }

    @PutMapping
    public Contrat updateContrat(@RequestBody Contrat contrat) {
        return icontratService.addEtModifyContrat(contrat);
    }

    @GetMapping
    public List<Contrat> getAllContrat() {
        return icontratService.getAllContrat();
    }

    @GetMapping("getConById/{id}")
    public Contrat getContratById(@PathVariable("id") int id) {
        return icontratService.getContratById(id);
    }

    @DeleteMapping("delById/{id}")
    public void deleteContratById(@PathVariable("id") int id) {
        icontratService.deleteContratById(id);
    }

    @PutMapping("assignContratCollab/{idContrat}/{idCollab}")
    public Contrat assignContratCollab(@PathVariable("idContrat") int idContrat, @PathVariable("idCollab") int idCollab) {
        return icontratService.assignContratCollaborator(idContrat, idCollab);
    }
}

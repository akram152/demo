package com.example.demo.controllers;

import com.example.demo.entities.Departement;
import com.example.demo.services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departement")
public class DepartementController {
    @Autowired
    private IDepartementService departementService;

    @PostMapping
    public Departement saveDepartement(@RequestBody Departement departement) {
        return departementService.saveOrUpdateDepartement (departement);
    }

    @PutMapping
    public void updateDepartement(@RequestBody Departement departement) {
        departementService.saveOrUpdateDepartement (departement);
    }
    //test valider
    @GetMapping("getid/{id}")
    public Departement getDepartementById(@PathVariable("id") int id) {
        return departementService.getDepartementById(id);
    }
    @GetMapping
    public List<Departement> getAllDepartement() {
        return departementService.getAllDepartements();
    }
    @GetMapping("getbyname/{name}")
    public List<Departement> getDepartementByName(@PathVariable("name") String name) {
        return  departementService.findDepartementByNomDepartement(name);
    }

    @DeleteMapping("deletebyid/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") int id) {
        departementService.DeleteDepartement(id);
        return ResponseEntity.ok("Supprimé");
    }

}
